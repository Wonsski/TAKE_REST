package com.example.demo.model.autobus;

import lombok.*;


@Getter @Setter
public class AutobusDTO {
    private Integer idAutobus;
    private String marka;
    private String model;
    private int liczbaMiejsc;

    public AutobusDTO(Autobus autobus) {
        this.idAutobus = autobus.getIdAutobus();
        this.marka = autobus.getMarka();
        this.model = autobus.getModel();
        this.liczbaMiejsc = autobus.getLiczbaMiejsc();
    }
}