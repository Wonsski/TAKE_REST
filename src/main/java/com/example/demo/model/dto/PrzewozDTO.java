package com.example.demo.model.dto;

import com.example.demo.controller.PrzewozController;
import com.example.demo.model.Przewoz;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Getter
public class PrzewozDTO extends RepresentationModel<PrzewozDTO> {
    private final Integer idPrzewoz;
    private final String dataWyjazdu;
    private final String dataPrzyjazdu;
    private final double cena;

    private final String trasaOpis;
    private final String autobusOpis;

    public PrzewozDTO(Przewoz p) {
        this.idPrzewoz = p.getIdPrzewoz();
        this.dataWyjazdu = p.getDataWyjazdu().toString();
        this.dataPrzyjazdu = p.getDataPrzyjazdu().toString();
        this.cena = p.getCena();

        this.trasaOpis = p.getTrasa().getMiejsceWyjazdu() + " → " + p.getTrasa().getMiejscePrzyjazdu();
        this.autobusOpis = p.getAutobus().getMarka() + " " + p.getAutobus().getModel();

        add(linkTo(methodOn(PrzewozController.class).getPrzewoz(idPrzewoz)).withSelfRel());
        add(linkTo(methodOn(PrzewozController.class).getAll()).withRel("wszystkie-przewozy"));
    }
}
