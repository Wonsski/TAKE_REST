package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Przewoz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idPrzewoz;

    @NotNull(message = "Trasa jest wymagana")
    @ManyToOne
    private Trasa trasa;

    @NotNull(message = "Autobus jest wymagany")
    @ManyToOne
    private Autobus autobus;

    @NotNull(message = "Data wyjazdu jest wymagana")
    private LocalDate dataWyjazdu;

    @NotNull(message = "Data przyjazdu jest wymagana")
    private LocalDate dataPrzyjazdu;

    @Min(value = 0, message = "Cena nie może być ujemna")
    private double cena;

    @ManyToMany
    @JoinTable(
        name = "rezerwacja",
        joinColumns = @JoinColumn(name = "przewoz_id"),
        inverseJoinColumns = @JoinColumn(name = "klient_id")
    )

    private List<Klient> klienci;
}
