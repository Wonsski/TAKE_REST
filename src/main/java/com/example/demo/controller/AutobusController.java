package com.example.demo.controller;

import com.example.demo.model.dto.AutobusDTO;
import com.example.demo.model.Autobus;
import com.example.demo.repository.AutobusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autobusy")
@RequiredArgsConstructor
public class AutobusController {

    private final AutobusRepository autobusRepo;

    // CREATE
    @PostMapping
    public AutobusDTO DodajAutobus(@RequestBody @Valid Autobus autobus) {
        try {
            Autobus zapisanyAutobus = autobusRepo.save(autobus);
            return new AutobusDTO(zapisanyAutobus);
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas zapisywania autobusu: " + e.getMessage());
        }
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                             "Nie znaleziono autobusu o ID " + id));
        return new AutobusDTO(a);
    }

    // UPDATE
    @PutMapping("/{id}")
    public AutobusDTO aktualizujAutobus(@PathVariable("id") Integer id, @RequestBody @Valid Autobus autobus) {
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
    public ResponseEntity<String> deleteAutobus(@PathVariable("id") Integer id) {
        if (!autobusRepo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Autobus o ID " + id + " nie istnieje");
        }

        autobusRepo.deleteById(id);
        return ResponseEntity.ok("Autobus o ID " + id + " został pomyślnie usunięty.");
    }

    // FILTER
    @GetMapping("/szukaj")
    public ResponseEntity<CollectionModel<AutobusDTO>> szukaj(
            @RequestParam(required = false) String marka,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String nrRej) {

        Specification<Autobus> spec = Specification.where(AutobusSpecifications.Marka(marka))
                                                    .and(AutobusSpecifications.Model(model))
                                                    .and(AutobusSpecifications.NrRej(nrRej));

        List<Autobus> wyniki = autobusRepo.findAll(spec);

        List<AutobusDTO> listaDTO = wyniki.stream().map(AutobusDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(listaDTO));
    }
}
class AutobusSpecifications {

    public static Specification<Autobus> Marka(String marka) {
        return (root, query, criteriaBuilder) -> {
            if (marka != null && !marka.isEmpty()) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("marka")), "%" + marka.toLowerCase() + "%");
            }
            return null;
        };
    }

    public static Specification<Autobus> Model(String model) {
        return (root, query, criteriaBuilder) -> {
            if (model != null && !model.isEmpty()) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("model")), "%" + model.toLowerCase() + "%");
            }
            return null;
        };
    }

    public static Specification<Autobus> NrRej(String nrRej) {
        return (root, query, criteriaBuilder) -> {
            if (nrRej != null && !nrRej.isEmpty()) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("nrRej")), "%" + nrRej.toLowerCase() + "%");
            }
            return null;
        };
    }
}
