package com.cib.customer.enums;

/**
 * Role of an individual corporate user within their organization.
 * Drives the maker-checker segregation of duties central to the
 * fund transfer approval workflow:
 *   MAKER   - can initiate/create fund transfers
 *   CHECKER - can approve, reject, or send back transfers for modification
 *   ADMIN   - manages customer/user records; typically does not transact
 */
public enum UserRole {
    MAKER,
    CHECKER,
    ADMIN
}
