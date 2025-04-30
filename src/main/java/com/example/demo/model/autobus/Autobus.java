package com.example.demo.model.autobus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Autobus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAutobus;

    private String marka;
    private String model;
    private String nrRej;
    private int liczbaMiejsc;
    
}
