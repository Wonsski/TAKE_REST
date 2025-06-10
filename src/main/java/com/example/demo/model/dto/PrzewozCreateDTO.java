package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PrzewozCreateDTO {

    @NotNull(message = "Data wyjazdu jest wymagana")
    @Schema(example = "2025-06-01")
    private final LocalDate dataWyjazdu;

    @NotNull(message = "Data przyjazdu jest wymagana")
    @Schema(example = "2025-06-01")
    private final LocalDate dataPrzyjazdu;

    @Positive(message = "Cena musi być większa od 0")
    @Schema(example = "49.99")
    private final double cena;

    @NotNull(message = "ID autobusu jest wymagane")
    @Schema(example = "2")
    private final Integer idAutobus;

    @NotNull(message = "ID trasy jest wymagane")
    @Schema(example = "2")
    private final Integer idTrasa;

    @JsonCreator
    public PrzewozCreateDTO(
            @JsonProperty("dataWyjazdu") LocalDate dataWyjazdu,
            @JsonProperty("dataPrzyjazdu") LocalDate dataPrzyjazdu,
            @JsonProperty("cena") double cena,
            @JsonProperty("idAutobus") Integer idAutobus,
            @JsonProperty("idTrasa") Integer idTrasa
    ) {
        this.dataWyjazdu = dataWyjazdu;
        this.dataPrzyjazdu = dataPrzyjazdu;
        this.cena = cena;
        this.idAutobus = idAutobus;
        this.idTrasa = idTrasa;
    }
}
