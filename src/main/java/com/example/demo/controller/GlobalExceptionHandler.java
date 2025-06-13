package com.example.demo.controller;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 400 - Walidacja danych wejściowych (np. @Valid)Add commentMore actions
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

    // 400 - Nieprawidłowy typ argumentu (np. złe ID w ścieżce)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Nieprawidłowy format ID: " + ex.getValue();
        return ResponseEntity.badRequest().body(Map.of("message", message));
    }

    // 404 - Brak zasobu (NoSuchElementException)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Nie znaleziono zasobu: " + ex.getMessage()));
    }

    // 400/404 - ResponseStatusException (np. trasa/autobus nie istnieje)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(Map.of("message", ex.getReason()));
    }

    // 500 - Błędy bazy danych
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, String>> handleDatabaseErrors(DataAccessException ex) {
        String message = "Błąd bazy danych: " + ex.getMostSpecificCause().getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", message));
    }

    // 500 - Inne nieoczekiwane błędy
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Błąd serwera: " + ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Błąd formatu danych wejściowych.";

        // Próba dokładniejszego rozpoznania błędu
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException formatException) {
            String fieldName = formatException.getPath().stream()

                    .map(ref -> ref.getFieldName())
                    .findFirst()
                    .orElse("nieznane pole");
            message = "Nieprawidłowy format pola: " + fieldName;
        }
        return ResponseEntity.badRequest().body(Map.of("message", message));
    }
}