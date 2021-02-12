package com.example.resource.server.config;

import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

@Configuration
public class Swagger2Config {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String DEFAULT_INCLUDE_PATTERN = ".*";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(File.class, URI.class, URL.class, InputStream.class)
                .useDefaultResponseMessages(false)
                .genericModelSubstitutes(Mono.class, Flux.class, Publisher.class).select()
                .apis(RequestHandlerSelectors.basePackage("com.example.resource.server.api"))
                .paths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
                .build().apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder()
                .title("Reactive Microservice RESTful API")
                .description("Microservice built on the basis of Spring Boot Reactive codebase")
                .version("1.0.0-SNAPSHOT").build();
    }

}