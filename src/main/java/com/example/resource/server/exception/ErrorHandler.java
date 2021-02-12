package com.example.resource.server.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 
 * Custom error handlers for different types of exception
 *  
 * @author akaliutau
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseStatusExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public Mono<ResponseEntity<String>> handle(AccessDeniedException ex) {
        log.error(ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<ApiException>> handle(RuntimeException ex) {
        log.error(ex.getMessage());
        List<String> errors = new ArrayList<>();
        ApiException apiError = new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errors);
        return Mono.just(new ResponseEntity<ApiException>(apiError, new HttpHeaders(), apiError.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ApiException>> handle(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        ApiException apiError = new ApiException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return Mono.just(new ResponseEntity<ApiException>(apiError, new HttpHeaders(), apiError.getStatus()));
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Mono<ResponseEntity<ApiException>> handle(MissingServletRequestParameterException ex) {
        log.error(ex.getMessage());
        List<String> errors = new ArrayList<>();
        ApiException apiError = new ApiException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return Mono.just(new ResponseEntity<ApiException>(apiError, new HttpHeaders(), apiError.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiException>> handle(Exception ex) {
        log.error(ex.getMessage());
        List<String> errors = new ArrayList<>();
        ApiException apiError = new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errors);

        return Mono.just(new ResponseEntity<ApiException>(apiError, new HttpHeaders(), apiError.getStatus()));
    }
}
