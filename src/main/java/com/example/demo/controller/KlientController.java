package com.example.demo.controller;

import com.example.demo.model.dto.KlientDTO;
import com.example.demo.model.Klient;
import com.example.demo.repository.KlientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/klienci")
@RequiredArgsConstructor
public class KlientController {

    private final KlientRepository klientRepo;

    // CREATE
    @PostMapping
    public KlientDTO addKlient(@RequestBody Klient klient) {
        return new KlientDTO(klientRepo.save(klient));
    }

    // READ ALL
    @GetMapping
    public CollectionModel<KlientDTO> getAll() {
        List<KlientDTO> lista = new ArrayList<>();
        for (Klient k : klientRepo.findAll()) {
            lista.add(new KlientDTO(k));
        }
        return CollectionModel.of(lista);
    }

    // READ ONE
    @GetMapping("/{id}")
    public KlientDTO getKlient(@PathVariable("id") Integer id) {
        Klient k = klientRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nie znaleziono klienta o ID " + id));
        return new KlientDTO(k);
    }

    // UPDATE
    @PutMapping("/{id}")
    public KlientDTO updateKlient(@PathVariable("id") Integer id, @RequestBody Klient klient) {
        Klient istniejący = klientRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nie znaleziono klienta o ID " + id));
        istniejący.setImie(klient.getImie());
        istniejący.setNazwisko(klient.getNazwisko());
        istniejący.setEmail(klient.getEmail());
        istniejący.setNrTel(klient.getNrTel());
        return new KlientDTO(klientRepo.save(istniejący));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteKlient(@PathVariable("id") Integer id) {
        if (!klientRepo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Nie można usunąć – klient o ID " + id + " nie istnieje");
        }
        klientRepo.deleteById(id);
    }

    // FILTER
    @GetMapping("/szukaj")
    public CollectionModel<KlientDTO> searchByEmail(@RequestParam("email") String email) {
        List<KlientDTO> lista = klientRepo.findByEmailContainingIgnoreCase(email)
                .stream()
                .map(KlientDTO::new)
                .toList();

        return CollectionModel.of(lista);
    }
}
