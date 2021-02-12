package com.example.resource.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A simple spring boot application build on the codebase Spring Boot 2.x + Reactive patterns
 * Can be considered as a resource server in the network of bigger cloud system
 * 
 * @author akaliutau
 */
@SpringBootApplication
public class CompleteResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompleteResourceServerApplication.class, args);
    }
}