package com.example.demo.controller;

import com.example.demo.model.Autobus;
import com.example.demo.repository.AutobusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autobusy")
@RequiredArgsConstructor
public class AutobusController {

    private final AutobusRepository autobusRepo;

    @PostMapping
    public Autobus dodajAutobus(@RequestBody Autobus autobus) {
        return autobusRepo.save(autobus);
    }

    @GetMapping
    public Iterable<Autobus> wszystkieAutobusy() {
        return autobusRepo.findAll();
    }
}
