package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Autobus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idAutobus;

    @NotBlank(message = "Marka jest wymagana")
    private String marka;

    @NotBlank(message = "Model jest wymagany")
    private String model;

    @NotBlank(message = "Numer rejestracyjny jest wymagany")
    @Size(min = 5, max = 10, message = "Numer rejestracyjny powinien mieć od 5 do 10 znaków")
    private String nrRej;

    @NotNull(message = "Liczba miejsc jest wymagana")
    @Min(value = 1, message = "Liczba miejsc musi być większa niż 0")
    private Integer liczbaMiejsc;
}
