package com.example.demo.model.dto;

import com.example.demo.controller.AutobusController;
import com.example.demo.model.Autobus;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Getter
public class AutobusDTO extends RepresentationModel<AutobusDTO> {
    private final Integer idAutobus;
    private final String marka;
    private final String model;
    private final String nrRej;
    private final Integer liczbaMiejsc;

    public AutobusDTO(Autobus autobus) {
        super();

        this.idAutobus = autobus.getIdAutobus();
        this.marka = autobus.getMarka();
        this.model = autobus.getModel();
        this.nrRej = autobus.getNrRej();
        this.liczbaMiejsc = autobus.getLiczbaMiejsc();

        add(linkTo(methodOn(AutobusController.class).getAutobus(idAutobus)).withSelfRel());
        add(linkTo(methodOn(AutobusController.class).getAll()).withRel("wszystkie-autobusy"));
    }
}
