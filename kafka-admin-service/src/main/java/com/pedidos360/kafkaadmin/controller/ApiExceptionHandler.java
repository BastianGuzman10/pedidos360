package com.pedidos360.kafkaadmin.controller;

import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    ResponseEntity<Map<String, String>> badRequest(Exception error) {
        return ResponseEntity.badRequest().body(Map.of("error", error.getMessage()));
    }

    @ExceptionHandler(ExecutionException.class)
    ResponseEntity<Map<String, String>> kafkaError(ExecutionException error) {
        Throwable cause = error.getCause();
        HttpStatus status = cause instanceof TopicExistsException ? HttpStatus.CONFLICT : HttpStatus.BAD_GATEWAY;
        return ResponseEntity.status(status).body(Map.of("error", cause == null ? error.getMessage() : cause.getMessage()));
    }
}
