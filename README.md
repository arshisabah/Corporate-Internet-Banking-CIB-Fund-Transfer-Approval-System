# CIB Fund Transfer & Approval System

Corporate Internet Banking platform: Java 17, Spring Boot 3, Spring Cloud, Maven multi-module, MySQL, OpenFeign + Eureka.

## Modules (build/startup order)

| # | Module | Port | Depends on |
|---|---|---|---|
| 1 | `cib-common` | - | (shared library, not a runnable service) |
| 2 | `config-server` | 8888 | none |
| 3 | `eureka-server` | 8761 | config-server |
| 4 | `customer-service` | 8081 | config-server, eureka-server, MySQL |
| 5 | `beneficiary-service` | 8082 | config-server, eureka-server, MySQL |
| 6 | `fund-transfer-service` | 8083 | config-server, eureka-server, MySQL, beneficiary-service, approval-service |
| 7 | `approval-service` | 8084 | config-server, eureka-server, MySQL, fund-transfer-service |
| 8 | `api-gateway` | 8080 | config-server, eureka-server |

## Local run order

```bash
# 1. Provision MySQL schemas (or let hibernate.ddl-auto=update create them)
mysql -u root -p < database/customer_service_db.sql
mysql -u root -p < database/beneficiary_service_db.sql
mysql -u root -p < database/fund_transfer_service_db.sql
mysql -u root -p < database/approval_service_db.sql

# 2. Build everything
mvn clean install

# 3. Start in order (separate terminals), waiting ~10s between each
cd config-server && mvn spring-boot:run
cd eureka-server && mvn spring-boot:run
cd customer-service && mvn spring-boot:run
cd beneficiary-service && mvn spring-boot:run
cd fund-transfer-service && mvn spring-boot:run
cd approval-service && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run
```

Eureka dashboard: http://localhost:8761
Swagger UI per service: http://localhost:<port>/swagger-ui.html
All external traffic via Gateway: http://localhost:8080/api/v1/...

## End-to-end happy path (via Gateway, port 8080)

```
1. POST /api/v1/customers                          -> onboard a CorporateCustomer
2. POST /api/v1/corporate-users                     -> create a MAKER user under that customer
3. POST /api/v1/beneficiaries                        -> register a beneficiary (defaults to ACTIVE)
4. POST /api/v1/transfers                             -> initiate transfer; validates beneficiary via Feign -> VALIDATED
5. POST /api/v1/transfers/{id}/submit                 -> submit to approval-service -> PENDING_APPROVAL
6. GET  /api/v1/approvals/pending                     -> checker queue
7. POST /api/v1/approvals/{approvalId}/approve        -> checker approves -> callback sets transfer APPROVED
8. PATCH /api/v1/transfers/{id}/status {"newStatus":"EXECUTED", "changedBy":"SYSTEM"}
                                                        -> marks transfer EXECUTED (execution engine out of scope)
9. GET  /api/v1/transfers/{id}/status-history          -> full audited transition trail
10. GET /api/v1/approvals/by-transfer/{transferId}/audit-trail -> compliance audit log
```

## Key architectural decisions (see inline Javadoc for detail)

- **Database-per-service**: 4 separate MySQL schemas; cross-service references (customerId, beneficiaryId, transferId) are plain columns, not FKs. Referential validation happens via Feign calls at write time.
- **Two-step transfer submission**: `POST /transfers` creates + validates beneficiary independently of approval-service's availability; `POST /transfers/{id}/submit` is the explicit, separate step that pushes to approval-service. See `fund-transfer-service`'s Javadoc.
- **State machine enforcement**: `TransferStatusValidator` (fund-transfer-service) is the single source of truth for legal `TransferStatus` transitions - both self-service calls and approval-service's callback route through it.
- **api-gateway does not depend on cib-common**: it runs on WebFlux/Netty (reactive), while cib-common pulls in servlet-stack starters (Web MVC, JPA, Security) that would conflict. The Gateway has its own minimal reactive error handler.
- **MAKER/CHECKER/ADMIN roles** (customer-service) exist now as a data model; enforcing "only a CHECKER may call /approve" via Spring Security/JWT is flagged as a natural next step, not yet implemented.
- **cib-common** provides: `ApiResponse<T>` envelope, exception hierarchy + `GlobalExceptionHandler`, `Auditable` base entity (createdAt/updatedAt/createdBy/updatedBy via JPA Auditing), all auto-registered via Spring Boot 3's `AutoConfiguration.imports`.

## Known gaps / natural next steps

- No Spring Security/JWT yet - `AuditorAwareImpl` defaults to `"SYSTEM"`; the internal-only endpoints (`PATCH /transfers/{id}/status`, `POST /approvals/requests`) are not access-restricted beyond network topology.
- `ddl-auto: update` is used for fast local iteration; switch to `validate` + Flyway/Liquibase migrations before any real deployment.
- No distributed tracing (Sleuth/Zipkin/Micrometer Tracing) wired in yet; `CorrelationIdGlobalFilter` in the Gateway is a first step toward that.
- No execution engine - "EXECUTED" is currently a manual status transition; a real core-banking execution call would replace step 8 above.
