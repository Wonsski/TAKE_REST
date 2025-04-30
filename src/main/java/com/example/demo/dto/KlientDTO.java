package com.example.demo.dto;

import com.example.demo.controller.KlientController;
import com.example.demo.model.Klient;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Getter
public class KlientDTO extends RepresentationModel<KlientDTO> {
    private final Integer idKlient;
    private final String imie;
    private final String nazwisko;
    private final String email;
    private final String nrTel;

    public KlientDTO(Klient klient) {
        this.idKlient = klient.getIdKlient();
        this.imie = klient.getImie();
        this.nazwisko = klient.getNazwisko();
        this.email = klient.getEmail();
        this.nrTel = klient.getNrTel();

        add(linkTo(methodOn(KlientController.class).getKlient(idKlient)).withSelfRel());
        add(linkTo(methodOn(KlientController.class).getAll()).withRel("wszyscy-klienci"));
    }
}
