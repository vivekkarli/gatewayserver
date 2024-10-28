package com.idealbank.gatewayserver.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator idealBankRouteConfig(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("accounts", r -> r
                        .path("/idealbank/accounts/**")
                        .filters(f -> f.rewritePath("/idealbank/accounts/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("accountsCircuitBreaker")
                                        .setFallbackUri("forward:/contact-support")))
                        .uri("lb://ACCOUNTS"))
                .route("cards", r -> r
                        .path("/idealbank/cards/**")
                        .filters(f -> f.rewritePath("/idealbank/cards/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://CARDS"))
                .route("loans", r -> r
                        .path("/idealbank/loans/**")
                        .filters(f -> f.rewritePath("/idealbank/cards/(?<segment>.*)","/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://LOANS"))
                .build();
    }
}
