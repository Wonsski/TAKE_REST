package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trasa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTrasa;

    private String miejsceWyjazdu;
    private String miejscePrzyjazdu;
    private double dystans;
    private double czasPodrozy;
}
