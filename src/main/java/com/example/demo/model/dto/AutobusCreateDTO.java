package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class AutobusCreateDTO {

    @NotBlank(message = "Marka jest wymagana")
    @Schema(example = "Mercedes")
    private final String marka;

    @NotBlank(message = "Model jest wymagany")
    @Schema(example = "Sprinter")
    private final String model;

    @NotBlank(message = "Numer rejestracyjny jest wymagany")
    @Size(min = 5, max = 10, message = "Numer rejestracyjny powinien mieć od 5 do 10 znaków")
    @Schema(example = "KR12345")
    private final String nrRej;

    @NotNull(message = "Liczba miejsc jest wymagana")
    @Min(value = 1, message = "Liczba miejsc musi być większa niż 0")
    @Schema(example = "20")
    private final Integer liczbaMiejsc;

    @JsonCreator
    public AutobusCreateDTO(
            @JsonProperty("marka") String marka,
            @JsonProperty("model") String model,
            @JsonProperty("nrRej") String nrRej,
            @JsonProperty("liczbaMiejsc") Integer liczbaMiejsc
    ) {
        this.marka = marka;
        this.model = model;
        this.nrRej = nrRej;
        this.liczbaMiejsc = liczbaMiejsc;
    }
}
