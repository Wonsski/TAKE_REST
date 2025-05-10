package com.example.demo.model.dto;

import com.example.demo.controller.AutobusController;
import com.example.demo.controller.KlientController;
import com.example.demo.controller.PrzewozController;
import com.example.demo.controller.TrasaController;
import com.example.demo.model.Klient;
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

        this.trasaOpis = (p.getTrasa() != null)
                ? p.getTrasa().getMiejsceWyjazdu() + " → " + p.getTrasa().getMiejscePrzyjazdu()
                : "Brak danych";

        this.autobusOpis = (p.getAutobus() != null)
                ? p.getAutobus().getMarka() + " " + p.getAutobus().getModel()
                : "Brak danych";

        // Linki
        add(linkTo(methodOn(PrzewozController.class).getPrzewoz(idPrzewoz)).withSelfRel());
        add(linkTo(methodOn(PrzewozController.class).getAll()).withRel("wszystkie-przewozy"));

        if (p.getAutobus() != null && p.getAutobus().getIdAutobus() != null) {
            add(linkTo(methodOn(AutobusController.class).getAutobus(p.getAutobus().getIdAutobus())).withRel("autobus"));
        }

        if (p.getTrasa() != null && p.getTrasa().getIdTrasa() != null) {
            add(linkTo(methodOn(TrasaController.class).getTrasa(p.getTrasa().getIdTrasa())).withRel("trasa"));
        }

        if (p.getKlienci() != null && !p.getKlienci().isEmpty()) {
            for (Klient klient : p.getKlienci()) {
                if (klient != null && klient.getIdKlient() != null) {
                    add(linkTo(methodOn(KlientController.class).getKlient(klient.getIdKlient())).withRel("klient-" + klient.getIdKlient()));
                }
            }
        }
    }
}
