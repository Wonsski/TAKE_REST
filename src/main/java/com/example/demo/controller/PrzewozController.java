package com.example.demo.controller;

import com.example.demo.dto.KlientDTO;
import com.example.demo.dto.PrzewozDTO;
import com.example.demo.model.Klient;
import com.example.demo.model.Przewoz;
import com.example.demo.repository.KlientRepository;
import com.example.demo.repository.PrzewozRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/przewozy")
@RequiredArgsConstructor
public class PrzewozController {

    private final PrzewozRepository przewozRepo;
    private final KlientRepository klientRepo;

    // CREATE
    @PostMapping
    public PrzewozDTO addPrzewoz(@RequestBody Przewoz przewoz) {
        return new PrzewozDTO(przewozRepo.save(przewoz));
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
    public PrzewozDTO updatePrzewoz(@PathVariable("id") Integer id, @RequestBody Przewoz nowy) {
        Przewoz p = przewozRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nie znaleziono przewozu o ID " + id));
        p.setDataWyjazdu(nowy.getDataWyjazdu());
        p.setDataPrzyjazdu(nowy.getDataPrzyjazdu());
        p.setCena(nowy.getCena());
        p.setTrasa(nowy.getTrasa());
        p.setAutobus(nowy.getAutobus());
        return new PrzewozDTO(przewozRepo.save(p));
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

    // FILTER
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
    public PrzewozDTO dodajKlientaDoPrzewozu(
            @PathVariable("idPrzewozu") Integer idPrzewozu,
            @PathVariable("idKlienta") Integer idKlienta) {

        Przewoz przewoz = przewozRepo.findById(idPrzewozu)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nie znaleziono przewozu o ID " + idPrzewozu));

        Klient klient = klientRepo.findById(idKlienta)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nie znaleziono klienta o ID " + idKlienta));

        if (!przewoz.getKlienci().contains(klient)) {
            przewoz.getKlienci().add(klient);
            klient.getPrzewozy().add(przewoz);
        }

        return new PrzewozDTO(przewozRepo.save(przewoz));
    }
}
