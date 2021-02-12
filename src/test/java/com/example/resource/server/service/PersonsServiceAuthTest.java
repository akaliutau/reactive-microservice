package com.example.resource.server.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.example.resource.server.CompleteResourceServerApplication;
import com.example.resource.server.data.Person;
import com.example.resource.server.data.PersonRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("Verify that book service")
@SpringJUnitConfig(CompleteResourceServerApplication.class)
public class PersonsServiceAuthTest {
    
    @Autowired
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private ReactiveJwtDecoder reactiveJwtDecoder;

    @DisplayName("grants access to create a record in DB for role 'DB_ADMIN'")
    @Test
    @WithMockUser(roles = "DB_ADMIN")
    void verifyCreateAccessIsGranted() {
        
        when(personRepository.saveAll(Mockito.<Mono<Person>>any())).thenReturn(Flux.just(new Person()));
        
        StepVerifier.create(personService.create(Mono.just(new Person(1L, "John", "Smith", 55, "red")))).verifyComplete();
    }
    
    @DisplayName("deny access to create a record in DB for role 'DB_USER'")
    @Test
    @WithMockUser(roles = "DB_USER")
    void verifyDenyAccessIsGranted() {
        
        when(personRepository.saveAll(Mockito.<Mono<Person>>any())).thenReturn(Flux.just(new Person()));
        
        StepVerifier.create(personService.create(Mono.just(new Person(1L, "John", "Smith", 55, "red")))).expectError();
    }
}
