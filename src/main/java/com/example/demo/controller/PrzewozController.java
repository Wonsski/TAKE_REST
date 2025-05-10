package com.example.demo.controller;

import com.example.demo.model.Autobus;
import com.example.demo.model.Trasa;
import com.example.demo.model.dto.KlientDTO;
import com.example.demo.model.dto.PrzewozDTO;
import com.example.demo.model.Klient;
import com.example.demo.model.Przewoz;
import com.example.demo.repository.AutobusRepository;
import com.example.demo.repository.KlientRepository;
import com.example.demo.repository.PrzewozRepository;
import com.example.demo.repository.TrasaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/przewozy")
@RequiredArgsConstructor
public class PrzewozController {

    private final PrzewozRepository przewozRepo;
    private final KlientRepository klientRepo;
    private final AutobusRepository autobusRepo;
    private final TrasaRepository trasaRepo;


    // CREATE
    @PostMapping
    public PrzewozDTO addPrzewoz(@Valid @RequestBody Przewoz przewoz) {
        try {
            validatePrzewoz(przewoz);

            Autobus autobus = autobusRepo.findById(przewoz.getAutobus().getIdAutobus())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autobus nie istnieje"));
            Trasa trasa = trasaRepo.findById(przewoz.getTrasa().getIdTrasa())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trasa nie istnieje"));

            przewoz.setAutobus(autobus);
            przewoz.setTrasa(trasa);

            Przewoz zapisany = przewozRepo.save(przewoz);
            return new PrzewozDTO(zapisany);
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas zapisywania przewozu: " + e.getMessage());
        }
    }

    // READ ALL
    @GetMapping
    public CollectionModel<PrzewozDTO> getAll() {
        List<PrzewozDTO> lista = new ArrayList<>();
        for (Przewoz p : przewozRepo.findAll()) {
            lista.add(new PrzewozDTO(p));
        }
        return CollectionModel.of(lista);
    }

    // READ ONE
    @GetMapping("/{id}")
    public PrzewozDTO getPrzewoz(@PathVariable("id") Integer id) {
        Przewoz p = przewozRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nie znaleziono przewozu o ID " + id));
        return new PrzewozDTO(p);
    }

    // UPDATE
    @PutMapping("/{id}")
    public PrzewozDTO updatePrzewoz(@PathVariable("id") Integer id, @Valid @RequestBody Przewoz nowy) {
        Przewoz istniejący = przewozRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nie znaleziono przewozu o ID " + id));
        validatePrzewoz(nowy);

        istniejący.setDataWyjazdu(nowy.getDataWyjazdu());
        istniejący.setDataPrzyjazdu(nowy.getDataPrzyjazdu());
        istniejący.setCena(nowy.getCena());
        istniejący.setTrasa(nowy.getTrasa());
        istniejący.setAutobus(nowy.getAutobus());

        return new PrzewozDTO(przewozRepo.save(istniejący));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deletePrzewoz(@PathVariable("id") Integer id) {
        if (!przewozRepo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Nie można usunąć – przewóz o ID " + id + " nie istnieje");
        }
        przewozRepo.deleteById(id);
    }

    // GET CLIENTS
    @GetMapping("/{id}/klienci")
    public CollectionModel<KlientDTO> getKlienciForPrzewoz(@PathVariable("id") Integer id) {
        Przewoz przewoz = przewozRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nie znaleziono przewozu o ID " + id));

        List<KlientDTO> lista = przewoz.getKlienci().stream()
                .map(KlientDTO::new)
                .toList();

        return CollectionModel.of(lista);
    }

    // ADD CLIENT
    @PostMapping("/{idPrzewozu}/dodaj-klienta/{idKlienta}")
    public PrzewozDTO dodajKlientaDoPrzewozu(@PathVariable("idPrzewozu") Integer idPrzewozu,
                                             @PathVariable("idKlienta") Integer idKlienta) {

        Przewoz przewoz = przewozRepo.findById(idPrzewozu)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nie znaleziono przewozu o ID " + idPrzewozu));

        Klient klient = klientRepo.findById(idKlienta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nie znaleziono klienta o ID " + idKlienta));

        if (przewoz.getKlienci().contains(klient)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Klient jest już zapisany na ten przewóz.");
        }

        int maxMiejsc = przewoz.getAutobus().getLiczbaMiejsc();
        if (przewoz.getKlienci().size() >= maxMiejsc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Brak dostępnych miejsc w autobusie.");
        }

        przewoz.getKlienci().add(klient);
        klient.getPrzewozy().add(przewoz);

        return new PrzewozDTO(przewozRepo.save(przewoz));
    }

    private void validatePrzewoz(Przewoz p) {
        if (p.getDataWyjazdu().isAfter(p.getDataPrzyjazdu())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data wyjazdu nie może być po dacie przyjazdu.");
        }
        if (p.getCena() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cena nie może być ujemna.");
        }
        if (!autobusRepo.existsById(p.getAutobus().getIdAutobus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autobus o podanym ID nie istnieje.");
        }
        if (!trasaRepo.existsById(p.getTrasa().getIdTrasa())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trasa o podanym ID nie istnieje.");
        }

        if (p.getKlienci() != null) {
            long count = p.getKlienci().stream().map(Klient::getIdKlient).distinct().count();
            if (count < p.getKlienci().size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lista klientów zawiera duplikaty.");
            }
        }
    }

    // FILTER
    // TODO: Rozbudowane filtrowanie
    @GetMapping("/szukaj")
    public CollectionModel<PrzewozDTO> filterByCena(
            @RequestParam("cenaMin") Double cenaMin,
            @RequestParam("cenaMax") Double cenaMax) {
        List<PrzewozDTO> lista = przewozRepo.findByCenaBetween(cenaMin, cenaMax)
                .stream()
                .map(PrzewozDTO::new)
                .toList();

        return CollectionModel.of(lista);
    }
}
