package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trasa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idTrasa;

    @NotBlank(message = "Miejsce wyjazdu nie może być puste")
    private String miejsceWyjazdu;

    @NotBlank(message = "Miejsce przyjazdu nie może być puste")
    private String miejscePrzyjazdu;

    @Min(value = 1, message = "Dystans musi być większy lub równy 1")
    private double dystans;

    @Min(value = 1, message = "Czas podróży musi być większy lub równy 1")
    private double czasPodrozy;
}
