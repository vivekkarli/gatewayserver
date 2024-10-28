package com.idealbank.gatewayserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/contact-support")
    public Mono<String> fallbackHandler(){

        return Mono.just("An error occured, please try again later. Or contact support team");

    }

}
