package com.example.resource.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.resource.server.data.Person;
import com.example.resource.server.data.PersonRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of business logic
 * 
 * @author akaliutau
 */
@Service
@PreAuthorize("hasAnyRole('LIBRARY_USER', 'DB_ADMIN')")
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Mono<Person> findById(long personId) {
        return personRepository.findById(personId);
    }

    public Flux<Person> findAll() {
        return personRepository.findAll();
    }

    @PreAuthorize("hasRole('DB_ADMIN')")
    public Mono<Void> create(Mono<Person> person) {
        return personRepository.saveAll(person).then();
    }
    
    @PreAuthorize("hasRole('DB_ADMIN')")
    public Mono<Void> update(Mono<Person> person) {
        return personRepository.saveAll(person).then();
    }


    @PreAuthorize("hasRole('DB_ADMIN')")
    public Mono<Void> deleteById(long personIdentifier) {
        return personRepository.deleteById(personIdentifier).then();
    }
}