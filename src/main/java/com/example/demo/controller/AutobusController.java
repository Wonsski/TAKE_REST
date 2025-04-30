package com.example.demo.controller;

import com.example.demo.model.autobus.Autobus;
import com.example.demo.model.autobus.AutobusDTO;
import com.example.demo.repository.AutobusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/autobusy")
@RequiredArgsConstructor
public class AutobusController {

    private final AutobusRepository autobusRepo;


    @PostMapping
    public ResponseEntity<AutobusDTO> dodajAutobus(@RequestBody Autobus autobus) {
        Autobus savedAutobus = autobusRepo.save(autobus);

        return ResponseEntity
                .created(linkTo(methodOn(AutobusController.class).getAutobus(savedAutobus.getIdAutobus())).toUri())
                .body(new AutobusDTO(savedAutobus));
    }

    // Pobranie konkretnego autobusu
    @GetMapping("/{idAutobus}")
    public ResponseEntity<?> getAutobus(@PathVariable Integer idAutobus) {
        Optional<Autobus> optionalAutobus = autobusRepo.findById(idAutobus);

        if (optionalAutobus.isPresent()) {
            AutobusDTO autobusDTO = new AutobusDTO(optionalAutobus.get());
            return ResponseEntity.ok(autobusDTO);
        } else {
            return ResponseEntity.ok("Nie znaleziono autobusu");
        }
    }

    @GetMapping
    public Iterable<AutobusDTO> getAutobusy() {
        return StreamSupport
                .stream(autobusRepo.findAll().spliterator(), false)
                .map(AutobusDTO::new)
                .collect(Collectors.toList());
    }

}

