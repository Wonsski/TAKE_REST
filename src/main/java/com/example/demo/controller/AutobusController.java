package com.example.demo.controller;

import com.example.demo.dto.AutobusDTO;
import com.example.demo.model.Autobus;
import com.example.demo.repository.AutobusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/autobusy")
@RequiredArgsConstructor
public class AutobusController {

    private final AutobusRepository autobusRepo;

    // CREATE
    @PostMapping
    public AutobusDTO addAutobus(@RequestBody Autobus autobus) {
        return new AutobusDTO(autobusRepo.save(autobus));
    }

    // READ ALL
    @GetMapping
    public CollectionModel<AutobusDTO> getAll() {
        List<AutobusDTO> lista = new ArrayList<>();
        for (Autobus a : autobusRepo.findAll()) {
            lista.add(new AutobusDTO(a));
        }
        return CollectionModel.of(lista);
    }

    // READ ONE
    @GetMapping("/{id}")
    public AutobusDTO getAutobus(@PathVariable("id") Integer id) {
        Autobus a = autobusRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nie znaleziono autobusu o ID " + id));
        return new AutobusDTO(a);
    }

    // UPDATE
    @PutMapping("/{id}")
    public AutobusDTO updateAutobus(@PathVariable("id") Integer id, @RequestBody Autobus autobus) {
        Autobus istniejący = autobusRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nie znaleziono autobusu o ID " + id));
        istniejący.setMarka(autobus.getMarka());
        istniejący.setModel(autobus.getModel());
        istniejący.setNrRej(autobus.getNrRej());
        istniejący.setLiczbaMiejsc(autobus.getLiczbaMiejsc());
        return new AutobusDTO(autobusRepo.save(istniejący));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteAutobus(@PathVariable("id") Integer id) {
        if (!autobusRepo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Nie można usunąć – autobus o ID " + id + " nie istnieje");
        }
        autobusRepo.deleteById(id);
    }

    // FILTER
    @GetMapping("/szukaj")
    public CollectionModel<AutobusDTO> searchByMarka(@RequestParam("marka") String marka) {
        List<AutobusDTO> lista = autobusRepo.findByMarkaContainingIgnoreCase(marka)
                .stream()
                .map(AutobusDTO::new)
                .toList();

        return CollectionModel.of(lista);
    }
}
