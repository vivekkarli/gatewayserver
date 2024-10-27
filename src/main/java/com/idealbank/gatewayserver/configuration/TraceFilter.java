package com.idealbank.gatewayserver.configuration;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class TraceFilter {

    private FilterUtility filterUtility;

    private static final Logger logger = LoggerFactory.getLogger(TraceFilter.class);


    @Bean
    @Order(1)
    public GlobalFilter requestTraceFilter() {

        return (exchange, chain) -> {

            HttpHeaders requestHeaders = exchange.getRequest().getHeaders();

            if (isCorrelationIdPresent(requestHeaders)) {
                logger.debug("idealBank-correlation-id found in RequestTraceFilter : {}",
                        filterUtility.getCorrelationId(requestHeaders));
            } else {
                String correlationID = generateCorrelationId();
                exchange = filterUtility.setCorrelationId(exchange, correlationID);
                logger.debug("idealBank-correlation-id generated in RequestTraceFilter : {}", correlationID);
            }

            return chain.filter(exchange);
        };

    }

    @Bean
    public GlobalFilter responseTraceFilter() {

        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                String correlationId = filterUtility.getCorrelationId(requestHeaders);
                logger.debug("Updated the correlation id to the outbound headers: {}", correlationId);
                exchange.getResponse().getHeaders().add(filterUtility.CORRELATION_ID, correlationId);
            }));
        };

    }

    private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
        if (filterUtility.getCorrelationId(requestHeaders) != null) {
            return true;
        } else {
            return false;
        }
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }

}
