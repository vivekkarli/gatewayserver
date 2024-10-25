package com.idealbank.gatewayserver.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator idealBankRouteConfig(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("accounts", r -> r
                        .path("/idealbank/accounts/**")
                        .filters(f -> f.rewritePath("/idealbank/accounts/(?<segment>.*)","/${segment}"))
                        .uri("lb://ACCOUNTS"))
                .route("cards", r -> r
                        .path("/idealbank/cards/**")
                        .filters(f -> f.rewritePath("/idealbank/cards/(?<segment>.*)","/${segment}"))
                        .uri("lb://CARDS"))
                .route("loans", r -> r
                        .path("/idealbank/loans/**")
                        .filters(f -> f.rewritePath("/idealbank/cards/(?<segment>.*)","/${segment}"))
                        .uri("lb://LOANS"))
                .build();
    }
}
