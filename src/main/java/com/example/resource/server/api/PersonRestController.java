package com.example.resource.server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.resource.server.data.Person;
import com.example.resource.server.service.PersonService;

import io.swagger.annotations.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** 
 * REST api for person DB service
 * 
 * @author akaliutau
 */
@RestController
@RequestMapping
@Validated
@Api(value = "Person Management System")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Access forbidden"), @ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
public class PersonRestController {

    private static final String PATH_VARIABLE_PERSON_ID = "personId";

    private static final String PATH_PERSON_ID = "{" + PATH_VARIABLE_PERSON_ID + "}";

    private final PersonService personService;

    @Autowired
    public PersonRestController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons")
    @ApiOperation(value = "Get all records (UserPermission: DB_USER)", response = Person.class)
    public Flux<Person> getAll() {
        return personService.findAll();
    }

    @GetMapping("/persons/" + PATH_PERSON_ID)
    @ApiOperation(value = "Get specific record (UserPermission: DB_USER)", response = Person.class)
    public Mono<ResponseEntity<Person>> getPersonById(@PathVariable(PATH_VARIABLE_PERSON_ID) long personId) {
        return personService.findById(personId).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/persons")
    @ApiOperation(value = "create record in Person database (UserPermission: DB_ADMIN)")
    public Mono<Void> create(@RequestBody Mono<Person> personResource) {
        return personService.create(personResource);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/persons")
    @ApiOperation(value = "update record in Person database (UserPermission: DB_ADMIN)")
    public Mono<Void> update(@RequestBody Mono<Person> personResource) {
        return personService.update(personResource);
    }

    @DeleteMapping("/persons/" + PATH_PERSON_ID)
    @ApiOperation(value = "delete record in Person database (UserPermission: DB_ADMIN)")
    public Mono<Void> delete(@PathVariable(PATH_VARIABLE_PERSON_ID) long personId) {
        return personService.deleteById(personId);
    }
}