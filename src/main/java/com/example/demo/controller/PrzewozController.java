package com.example.demo.controller;

import com.example.demo.model.autobus.Autobus;
import com.example.demo.model.klient.Klient;
import com.example.demo.model.trasa.Trasa;
import com.example.demo.model.przewoz.Przewoz;
import com.example.demo.model.przewoz.PrzewozDTO;
import com.example.demo.repository.KlientRepository;
import com.example.demo.repository.PrzewozRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@RequestMapping("/przewozy")
public class PrzewozController {

    private final PrzewozRepository przewozRepo;
    private final KlientRepository klientRepo;

    public PrzewozController(PrzewozRepository przewozRepo, KlientRepository klientRepo) {
        this.przewozRepo = przewozRepo;
        this.klientRepo = klientRepo;
    }

    @PostMapping
    public ResponseEntity<PrzewozDTO> dodajPrzewoz(@RequestBody Przewoz przewoz) {
        Przewoz saved = przewozRepo.save(przewoz);
        return ResponseEntity.ok(new PrzewozDTO(saved));
    }

    @GetMapping
    public ResponseEntity<List<PrzewozDTO>> wszystkiePrzewozy() {
        List<PrzewozDTO> dtoList = StreamSupport.stream(przewozRepo.findAll().spliterator(), false)
                .map(PrzewozDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/{idPrzewozu}/dodaj-klienta/{idKlienta}")
    public ResponseEntity<PrzewozDTO> dodajKlientaDoPrzewozu(
            @PathVariable Integer idPrzewozu,
            @PathVariable Integer idKlienta
    ) {
        Przewoz przewoz = przewozRepo.findById(idPrzewozu)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono przewozu"));

        Klient klient = klientRepo.findById(idKlienta)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono klienta"));

        if (!przewoz.getKlienci().contains(klient)) {
            przewoz.getKlienci().add(klient);
            klient.getPrzewozy().add(przewoz); // jeśli relacja dwukierunkowa
        }

        Przewoz updated = przewozRepo.save(przewoz);
        return ResponseEntity.ok(new PrzewozDTO(updated));
    }

    @GetMapping("/{id}/trasa")
    public ResponseEntity<Trasa> getTrasa(@PathVariable Integer id) {
        Przewoz p = przewozRepo.findById(id).orElseThrow();
        return ResponseEntity.ok(p.getTrasa());
    }

    @GetMapping("/{id}/autobus")
    public ResponseEntity<Autobus> getAutobus(@PathVariable Integer id) {
        Przewoz p = przewozRepo.findById(id).orElseThrow();
        return ResponseEntity.ok(p.getAutobus());
    }

    @GetMapping("/{id}/klienci")
    public ResponseEntity<List<Klient>> getKlienci(@PathVariable Integer id) {
        Przewoz p = przewozRepo.findById(id).orElseThrow();
        return ResponseEntity.ok(p.getKlienci());
    }
}