package com.example.resource.server.api;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.BodyInserters;

import com.example.resource.server.data.Person;
import com.example.resource.server.service.PersonService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@ActiveProfiles("test")
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
@WebFluxTest
@WithMockUser
@DisplayName("Verify book api")
public class ApiIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    private MockMvc mvc;

    private WebTestClient webTestClient;
    
    @MockBean 
    private PersonService personService;

    @MockBean 
    private ReactiveJwtDecoder reactiveJwtDecoder;

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) {
        this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext)
                .apply(springSecurity()).configureClient()
                .baseUrl("http://localhost:9090")
                .filter(
                        documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                 .withResponseDefaults(prettyPrint()))
                .build();
        System.out.println("before:"+this.webTestClient);
    }

    @Test
    @DisplayName("functional test")
    public void redirectToLogin() {
        this.webTestClient.get().uri("/").exchange().expectStatus().is4xxClientError();
    }

  
    @Test
    @DisplayName("get list of persons")
    public void getAll() throws Exception {
        Long pid = 1L;
        String lastName = "lastName";

        given(personService.findAll())
            .willReturn(
                Flux.just(
                    Person.builder().firstName("firstName").lastName(lastName).favouriteColor("red").age(22).id(pid).build()));

        webTestClient
            .get()
            .uri("/persons")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .json("[{\"id\":1,\"first_name\":\"firstName\",\"last_name\":\"lastName\",\"age\":22,\"favourite_color\":\"red\"}]")
            .consumeWith(
                document(
                    "get-persons", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));

    }
    
    @Test
    @DisplayName("get person by id")
    public void getById() throws Exception {
        Long pid = 1L;
        String lastName = "lastName";

        given(personService.findById(pid))
            .willReturn(
                Mono.just(
                    Person.builder().firstName("firstName").lastName(lastName).favouriteColor("red").age(22).id(pid).build()));

        webTestClient
            .get()
            .uri("/persons/"+ pid)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .json("{\"id\":1,\"first_name\":\"firstName\",\"last_name\":\"lastName\",\"age\":22,\"favourite_color\":\"red\"}")
            .consumeWith(
                document(
                    "get-persons", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));

    }
    
    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("create a person record")
    @WithMockUser(roles = {"DB_ADMIN"})
    public void create() throws Exception {
        Long pid = 1L;
        String lastName = "lastName";
        
        given(personService.create(any())).willAnswer(b -> Mono.empty());
        
        Person p = Person.builder().firstName("firstName").lastName(lastName).favouriteColor("red").age(22).id(pid).build();

        webTestClient
            .mutateWith(csrf())
            .post()
            .uri("/persons/")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromObject(p))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody()
            .consumeWith(
                document(
                    "create-book",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())));
        
        ArgumentCaptor<Mono<Person>> bookArg = ArgumentCaptor.forClass(Mono.class);
        verify(personService).create(bookArg.capture());

        assertThat(bookArg.getValue().block()).isNotNull().isEqualTo(p);

    }
    
    @Test
    @DisplayName("test of person record deletion")
    void verifyAndDocumentDeleteBook() {

      Long pid = 1L;
      given(personService.deleteById(pid)).willReturn(Mono.empty());

      webTestClient
          .mutateWith(csrf())
          .delete()
          .uri("/persons/{pid}", pid)
          .accept(MediaType.APPLICATION_JSON)
          .exchange()
          .expectStatus()
          .isOk()
          .expectBody()
          .consumeWith(
              document(
                  "delete-book",
                  preprocessRequest(prettyPrint()),
                  preprocessResponse(prettyPrint())));
    }

}
