package com.example.demo.model.przewoz;

import com.example.demo.controller.PrzewozController;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.*;

import com.example.demo.model.klient.Klient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Getter @Setter
public class PrzewozDTO extends RepresentationModel<PrzewozDTO> {

    private Integer idPrzewoz;
    private LocalDate dataWyjazdu;
    private LocalDate dataPrzyjazdu;
    private double cena;

    private Integer trasaId;
    private Integer autobusId;
    private List<Integer> klienciIds;

    public PrzewozDTO(Przewoz przewoz) {
        super();
        this.idPrzewoz = przewoz.getIdPrzewoz();
        this.dataWyjazdu = przewoz.getDataWyjazdu();
        this.dataPrzyjazdu = przewoz.getDataPrzyjazdu();
        this.cena = przewoz.getCena();
        this.trasaId = przewoz.getTrasa() != null ? przewoz.getTrasa().getIdTrasa() : null;
        this.autobusId = przewoz.getAutobus() != null ? przewoz.getAutobus().getIdAutobus() : null;
        this.klienciIds = przewoz.getKlienci() != null ?
                przewoz.getKlienci().stream()
                        .map(Klient::getIdKlient)
                        .collect(Collectors.toList()) : new ArrayList<>();


        this.add(linkTo(methodOn(PrzewozController.class).getTrasa(przewoz.getIdPrzewoz())).withRel("trasa"));
        this.add(linkTo(methodOn(PrzewozController.class).getAutobus(przewoz.getIdPrzewoz())).withRel("autobus"));
        this.add(linkTo(methodOn(PrzewozController.class).getKlienci(przewoz.getIdPrzewoz())).withRel("klienci"));
    }
}
