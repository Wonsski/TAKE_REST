package com.example.demo.model.przewoz;

import com.example.demo.model.autobus.Autobus;
import com.example.demo.model.klient.Klient;
import com.example.demo.model.trasa.Trasa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
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
