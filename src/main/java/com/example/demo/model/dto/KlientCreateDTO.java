package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class KlientCreateDTO {

    @NotBlank(message = "Imię jest wymagane")
    @Schema(example = "Jan")
    private final String imie;

    @NotBlank(message = "Nazwisko jest wymagane")
    @Schema(example = "Kowalski")
    private final String nazwisko;

    @Email(message = "Nieprawidłowy format adresu e-mail")
    @NotBlank(message = "Email jest wymagany")
    @Schema(example = "jan.kowalski@example.com")
    private final String email;

    @Pattern(regexp = "\\d{9}", message = "Numer telefonu powinien mieć dokładnie 9 cyfr")
    @NotBlank(message = "Numer telefonu jest wymagany")
    @Schema(example = "123456789")
    private final String nrTel;

    @JsonCreator
    public KlientCreateDTO(
            @JsonProperty("imie") String imie,
            @JsonProperty("nazwisko") String nazwisko,
            @JsonProperty("email") String email,
            @JsonProperty("nrTel") String nrTel
    ) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.nrTel = nrTel;
    }
}
