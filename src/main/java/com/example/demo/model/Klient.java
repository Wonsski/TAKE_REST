package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Klient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idKlient;

    @NotBlank(message = "Imię jest wymagane")
    private String imie;

    @NotBlank(message = "Nazwisko jest wymagane")
    private String nazwisko;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Nieprawidłowy format adresu e-mail")
    private String email;

    @NotBlank(message = "Numer telefonu jest wymagany")
    @Pattern(regexp = "\\d{9}", message = "Numer telefonu powinien mieć dokładnie 9 cyfr")
    private String nrTel;

    @ManyToMany(mappedBy = "klienci")
    @JsonIgnore
    private List<Przewoz> przewozy;
}
