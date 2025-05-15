package com.example.demo.model.dto;

import com.example.demo.controller.TrasaController;
import com.example.demo.model.Trasa;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Getter
public class TrasaDTO extends RepresentationModel<TrasaDTO> {
    private final Integer idTrasa;
    private final String miejsceWyjazdu;
    private final String miejscePrzyjazdu;
    private final double dystans;
    private final double czasPodrozy;

    public TrasaDTO(Trasa trasa) {
        this.idTrasa = trasa.getIdTrasa();
        this.miejsceWyjazdu = trasa.getMiejsceWyjazdu();
        this.miejscePrzyjazdu = trasa.getMiejscePrzyjazdu();
        this.dystans = trasa.getDystans();
        this.czasPodrozy = trasa.getCzasPodrozy();

        add(linkTo(methodOn(TrasaController.class).getTrasa(idTrasa)).withSelfRel());
        add(linkTo(methodOn(TrasaController.class).getAll()).withRel("wszystkie-trasy"));
    }
}
