package com.example.demo.controller;

import com.example.demo.model.Klient;
import com.example.demo.model.Przewoz;
import com.example.demo.repository.KlientRepository;
import com.example.demo.repository.PrzewozRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/przewozy")
@RequiredArgsConstructor
public class PrzewozController {

    private final PrzewozRepository przewozRepo;
    private final KlientRepository klientRepo;

    @PostMapping
    public Przewoz dodajPrzewoz(@RequestBody Przewoz przewoz) {
        return przewozRepo.save(przewoz);
    }

    @GetMapping
    public Iterable<Przewoz> wszystkiePrzewozy() {
        return przewozRepo.findAll();
    }
    
    @PostMapping("/{idPrzewozu}/dodaj-klienta/{idKlienta}")
    public Przewoz dodajKlientaDoPrzewozu(
    		@PathVariable("idPrzewozu") Integer idPrzewozu,
            @PathVariable("idKlienta") Integer idKlienta
    ) {
        Przewoz przewoz = przewozRepo.findById(idPrzewozu)
            .orElseThrow(() -> new RuntimeException("Nie znaleziono przewozu"));

        Klient klient = klientRepo.findById(idKlienta)
            .orElseThrow(() -> new RuntimeException("Nie znaleziono klienta"));

        if (!przewoz.getKlienci().contains(klient)) {
            przewoz.getKlienci().add(klient);
            klient.getPrzewozy().add(przewoz);
        }

        return przewozRepo.save(przewoz);
    }

}
