package com.example.demo.controller;


import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - Walidacja danych wejściowych
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();

            errors.computeIfAbsent(field, key -> new ArrayList<>()).add(message);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    // 400 - Nieprawidłowy typ argumentu (np. złe ID)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Nieprawidłowy format ID: " + ex.getValue();
        return ResponseEntity.badRequest().body(message);
    }

    // 404 - Brak zasobu
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Nie znaleziono zasobu: " + ex.getMessage());
    }

    // 500 - Błędy bazy danych
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDatabaseErrors(DataAccessException ex) {
        String message = "Błąd bazy danych: " + ex.getMostSpecificCause().getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    // 500 - Inne nieoczekiwane błędy
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Błąd serwera: " + ex.getMessage());
    }
}
