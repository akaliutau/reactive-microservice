package com.example.resource.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.resource.server.data.Person;
import com.example.resource.server.data.PersonRepository;
import com.example.resource.server.service.PersonService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of business logic
 * 
 * @author akaliutau
 */
@Service
@PreAuthorize("hasAnyRole('DB_USER', 'DB_ADMIN')")
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Mono<Person> findById(long personId) {
        return personRepository.findById(personId);
    }

    @Override
    public Flux<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('DB_ADMIN')")
    public Mono<Void> create(Mono<Person> person) {
        return personRepository.saveAll(person).then();
    }
    
    @Override
    @PreAuthorize("hasRole('DB_ADMIN')")
    public Mono<Void> update(Mono<Person> person) {
        return personRepository.saveAll(person).then();
    }


    @Override
    @PreAuthorize("hasRole('DB_ADMIN')")
    public Mono<Void> deleteById(long personIdentifier) {
        return personRepository.deleteById(personIdentifier).then();
    }
}