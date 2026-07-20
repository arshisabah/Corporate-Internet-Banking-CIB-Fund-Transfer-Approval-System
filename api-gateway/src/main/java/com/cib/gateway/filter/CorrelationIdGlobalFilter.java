package com.cib.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Stamps every request passing through the Gateway with an X-Correlation-Id
 * header (reused if the caller already supplied one, generated otherwise)
 * and logs method/path/status/duration. This correlation ID should be
 * propagated by each downstream service's logging MDC in a full production
 * setup so a single request can be traced across all 4 business services
 * from the logs alone - essential once Kafka/async tracing isn't in play.
 */
@Component
public class CorrelationIdGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger log = Logger.getLogger(CorrelationIdGlobalFilter.class.getName());
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String correlationId = request.getHeaders().getFirst(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        final String finalCorrelationId = correlationId;

        ServerHttpRequest mutatedRequest = request.mutate()
                .header(CORRELATION_ID_HEADER, finalCorrelationId)
                .build();
        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

        long startTime = System.currentTimeMillis();
        log.info(() -> String.format("[%s] --> %s %s", finalCorrelationId,
                request.getMethod(), request.getURI().getPath()));

        return chain.filter(mutatedExchange).then(Mono.fromRunnable(() -> {
            long duration = System.currentTimeMillis() - startTime;
            log.info(() -> String.format("[%s] <-- %s %s completed in %dms",
                    finalCorrelationId, request.getMethod(), request.getURI().getPath(), duration));
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
