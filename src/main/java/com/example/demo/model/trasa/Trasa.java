package com.example.demo.model.trasa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Trasa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTrasa;

    private String miejsceWyjazdu;
    private String miejscePrzyjazdu;
    private double dystans;
    private double czasPodrozy;
}
