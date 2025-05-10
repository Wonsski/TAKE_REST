package com.example.demo.controller;

import com.example.demo.model.Przewoz;
import com.example.demo.model.dto.TrasaDTO;
import com.example.demo.model.Trasa;
import com.example.demo.repository.PrzewozRepository;
import com.example.demo.repository.TrasaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.Optional;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/trasy")
@RequiredArgsConstructor
public class TrasaController {

    private final TrasaRepository trasaRepo;
    private final PrzewozRepository przewozRepo;

    // CREATE
    @PostMapping
    public TrasaDTO addTrasa(@Valid @RequestBody Trasa trasa) {
        try {
            Trasa newTrasa = trasaRepo.save(trasa);
            return new TrasaDTO(newTrasa);
        }catch (Exception e){
            throw new RuntimeException("Błąd podczas zapisywania trasy: "+ e.getMessage());
        }
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
    public TrasaDTO updateTrasa(@PathVariable("id") Integer id, @Valid @RequestBody Trasa trasa) {
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
    public ResponseEntity<?> deleteTrasa(@PathVariable("id") Integer id) {
        Optional<Trasa> trasaOpt = trasaRepo.findById(id);

        if (trasaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nie można usunąć – trasa o ID " + id + " nie istnieje");
        }

        Trasa trasa = trasaOpt.get();

        List<Przewoz> powiazanePrzewozy = StreamSupport
                .stream(przewozRepo.findAll().spliterator(), false)
                .filter(p -> p.getTrasa().getIdTrasa().equals(id))
                .toList();

        if (!powiazanePrzewozy.isEmpty()) {
            List<Integer> idPrzewozow = powiazanePrzewozy.stream()
                    .map(Przewoz::getIdPrzewoz)
                    .toList();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Nie można usunąć – trasa jest używana w przewozach o ID: " + idPrzewozow +
                            ". Usuń te przewozy przed usunięciem trasy.");
        }

        trasaRepo.deleteById(id);
        return ResponseEntity.ok("Trasa o ID " + id + " została usunięta");
    }

    // FILTER
    // TODO: Rozbudowane filtrowanie
    @GetMapping("/szukaj")
    public CollectionModel<TrasaDTO> searchByWyjazd(@RequestParam("miejsceWyjazdu") String miejsceWyjazdu) {
        List<TrasaDTO> lista = trasaRepo.findByMiejsceWyjazduContainingIgnoreCase(miejsceWyjazdu)
                .stream()
                .map(TrasaDTO::new)
                .toList();
        return CollectionModel.of(lista);
    }
}
