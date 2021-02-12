package com.example.resource.server.service;

import com.example.resource.server.data.Person;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonService {

    Mono<Person> findById(long personId);

    Flux<Person> findAll();

    Mono<Void> create(Mono<Person> person);

    Mono<Void> update(Mono<Person> person);

    Mono<Void> deleteById(long personIdentifier);

}