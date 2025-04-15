package com.example.demo.controller;

import com.example.demo.model.Trasa;
import com.example.demo.repository.TrasaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trasy")
@RequiredArgsConstructor
public class TrasaController {

    private final TrasaRepository trasaRepo;

    @PostMapping
    public Trasa dodajTrase(@RequestBody Trasa trasa) {
        return trasaRepo.save(trasa);
    }

    @GetMapping
    public Iterable<Trasa> wszystkieTrasy() {
        return trasaRepo.findAll();
    }
}
