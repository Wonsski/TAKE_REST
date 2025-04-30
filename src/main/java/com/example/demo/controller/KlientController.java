package com.example.demo.controller;

import com.example.demo.model.klient.Klient;
import com.example.demo.repository.KlientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/klienci")
@RequiredArgsConstructor
public class KlientController {

    private final KlientRepository klientRepo;

    @PostMapping
    public Klient dodajKlienta(@RequestBody Klient klient) {
        return klientRepo.save(klient);
    }

    @GetMapping
    public Iterable<Klient> wszyscyKlienci() {
        return klientRepo.findAll();
    }
}
