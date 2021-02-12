package com.example.resource.server.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {

    @Id
    private Long id;

    @Column(value = "first_name")
    @JsonProperty("first_name")
    String firstName;

    @Column(value = "last_name")
    @JsonProperty("last_name")
    String lastName;

    @Column(value = "age")
    @JsonProperty("age")
    Integer age;

    @Column(value = "favourite_color")
    @JsonProperty("favourite_color")
    String favouriteColor;

}