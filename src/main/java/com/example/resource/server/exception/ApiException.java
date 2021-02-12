package com.example.resource.server.exception;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class for exception
 * 
 * @author akaliutau
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiException {
    private HttpStatus status;
    private String message;
    private List<String> details;
    private final Date date = new Date();
}
