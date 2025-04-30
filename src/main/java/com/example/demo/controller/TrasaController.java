package com.example.demo.controller;

import com.example.demo.model.dto.TrasaDTO;
import com.example.demo.model.Trasa;
import com.example.demo.repository.TrasaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/trasy")
@RequiredArgsConstructor
public class TrasaController {

    private final TrasaRepository trasaRepo;

    // CREATE
    @PostMapping
    public TrasaDTO addTrasa(@RequestBody Trasa trasa) {
        return new TrasaDTO(trasaRepo.save(trasa));
    }

    // READ ALL
    @GetMapping
    public CollectionModel<TrasaDTO> getAll() {
        List<TrasaDTO> lista = new ArrayList<>();
        for (Trasa t : trasaRepo.findAll()) {
            lista.add(new TrasaDTO(t));
        }
        return CollectionModel.of(lista);
    }

    // READ ONE
    @GetMapping("/{id}")
    public TrasaDTO getTrasa(@PathVariable("id") Integer id) {
        Trasa t = trasaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nie znaleziono trasy o ID " + id));
        return new TrasaDTO(t);
    }

    // UPDATE
    @PutMapping("/{id}")
    public TrasaDTO updateTrasa(@PathVariable("id") Integer id, @RequestBody Trasa trasa) {
        Trasa istniejąca = trasaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nie znaleziono trasy o ID " + id));
        istniejąca.setMiejsceWyjazdu(trasa.getMiejsceWyjazdu());
        istniejąca.setMiejscePrzyjazdu(trasa.getMiejscePrzyjazdu());
        istniejąca.setDystans(trasa.getDystans());
        istniejąca.setCzasPodrozy(trasa.getCzasPodrozy());
        return new TrasaDTO(trasaRepo.save(istniejąca));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteTrasa(@PathVariable("id") Integer id) {
        if (!trasaRepo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Nie można usunąć – trasa o ID " + id + " nie istnieje");
        }
        trasaRepo.deleteById(id);
    }

    // FILTER
    @GetMapping("/szukaj")
    public CollectionModel<TrasaDTO> searchByWyjazd(@RequestParam("miejsceWyjazdu") String miejsceWyjazdu) {
        List<TrasaDTO> lista = trasaRepo.findByMiejsceWyjazduContainingIgnoreCase(miejsceWyjazdu)
                .stream()
                .map(TrasaDTO::new)
                .toList();
        return CollectionModel.of(lista);
    }
}
