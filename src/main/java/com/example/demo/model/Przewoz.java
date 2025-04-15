package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Przewoz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPrzewoz;

    @ManyToOne
    private Trasa trasa;

    @ManyToOne
    private Autobus autobus;

    private LocalDate dataWyjazdu;
    private LocalDate dataPrzyjazdu;
    private double cena;

    @ManyToMany
    @JoinTable(
        name = "rezerwacja",
        joinColumns = @JoinColumn(name = "przewoz_id"),
        inverseJoinColumns = @JoinColumn(name = "klient_id")
    )
    private List<Klient> klienci;
}
