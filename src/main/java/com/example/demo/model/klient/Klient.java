package com.example.demo.model.klient;

import com.example.demo.model.przewoz.Przewoz;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Klient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idKlient;

    private String imie;
    private String nazwisko;
    private String email;
    private String nrTel;

    @ManyToMany(mappedBy = "klienci")
    @JsonIgnore
    private List<Przewoz> przewozy;
}
