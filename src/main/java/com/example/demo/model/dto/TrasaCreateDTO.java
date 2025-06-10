package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class TrasaCreateDTO {

    @NotBlank(message = "Miejsce wyjazdu nie może być puste")
    @Schema(example = "Kraków")
    private final String miejsceWyjazdu;

    @NotBlank(message = "Miejsce przyjazdu nie może być puste")
    @Schema(example = "Warszawa")
    private final String miejscePrzyjazdu;

    @Min(value = 1, message = "Dystans musi być większy lub równy 1")
    @Schema(example = "300.0")
    private final double dystans;

    @Min(value = 1, message = "Czas podróży musi być większy lub równy 1")
    @Schema(example = "4.5")
    private final double czasPodrozy;

    @JsonCreator
    public TrasaCreateDTO(
            @JsonProperty("miejsceWyjazdu") String miejsceWyjazdu,
            @JsonProperty("miejscePrzyjazdu") String miejscePrzyjazdu,
            @JsonProperty("dystans") double dystans,
            @JsonProperty("czasPodrozy") double czasPodrozy
    ) {
        this.miejsceWyjazdu = miejsceWyjazdu;
        this.miejscePrzyjazdu = miejscePrzyjazdu;
        this.dystans = dystans;
        this.czasPodrozy = czasPodrozy;
    }
}
