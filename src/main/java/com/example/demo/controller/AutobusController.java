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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/autobusy")
@RequiredArgsConstructor
public class AutobusController {

    private final AutobusRepository autobusRepo;

    // CREATE
    @PostMapping
    public AutobusDTO addAutobus(@RequestBody @Valid Autobus autobus) {
        try {
            Autobus zapisanyAutobus = autobusRepo.save(autobus);
            return new AutobusDTO(zapisanyAutobus);
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas zapisywania autobusu: " + e.getMessage());
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<AutobusDTO> lista = StreamSupport
                .stream(autobusRepo.findAll().spliterator(), false)
                .map(AutobusDTO::new)
                .toList();

        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Brak autobusów w bazie");
        }

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getAutobus(@PathVariable("id") Integer id) {
        Optional<Autobus> optionalAutobus = autobusRepo.findById(id);

        if (optionalAutobus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("nie znaleziono Autobusu o ID: " + id);
        }

        AutobusDTO dto = new AutobusDTO(optionalAutobus.get());
        return ResponseEntity.ok(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> uppdateAutobus(@PathVariable("id") Integer id, @RequestBody @Valid Autobus autobus) {
        if (autobus == null) {
            return ResponseEntity.badRequest().body("Brak danych klienta w żądaniu");
        }
        Optional<Autobus> optionalAutobus = autobusRepo.findById(id);
        if (optionalAutobus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nie znaleziono autobusu o ID " + id);
        }
        Autobus existing = optionalAutobus.get();

        existing.setMarka(autobus.getMarka());
        existing.setModel(autobus.getModel());
        existing.setNrRej(autobus.getNrRej());
        existing.setLiczbaMiejsc(autobus.getLiczbaMiejsc());
        AutobusDTO dto = new AutobusDTO(autobusRepo.save(existing));
        return ResponseEntity.ok(dto);
    }

    // DELETE
    public ResponseEntity<?> deleteAutobus(@PathVariable("id") Integer id) {
        if (!autobusRepo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Autobus o ID " + id + " nie istnieje");
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

        List<Autobus> output = autobusRepo.findAll(spec);

        List<AutobusDTO> dtoList = output.stream().map(AutobusDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(dtoList));
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
