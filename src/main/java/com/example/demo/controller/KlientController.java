package com.example.demo.controller;

import com.example.demo.model.Przewoz;
import com.example.demo.model.dto.KlientDTO;
import com.example.demo.model.Klient;
import com.example.demo.repository.KlientRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/klienci")
@RequiredArgsConstructor
public class KlientController {

    private final KlientRepository klientRepo;

    private ResponseEntity<Map<String, String>> errorResponse(HttpStatus status, String msg) {
        Map<String, String> err = new HashMap<>();
        err.put("message", msg);
        return ResponseEntity.status(status).body(err);
    }

    // CREATE
    @PostMapping
    public KlientDTO addKlient(@RequestBody @Valid Klient klient) {
        try {
            Klient newklient = klientRepo.save(klient);
            return new KlientDTO(klientRepo.save(newklient));
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas zapisywania klienta: " + e.getMessage());
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<KlientDTO> lista = StreamSupport
                .stream(klientRepo.findAll().spliterator(), false)
                .map(KlientDTO::new)
                .toList();

        if (lista.isEmpty()) {
            return errorResponse(HttpStatus.NOT_FOUND, "Brak klientów w bazie");
        }

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getKlient(@PathVariable("id") Integer id) {
        Optional<Klient> optionalKlient = klientRepo.findById(id);

        if (optionalKlient.isEmpty()) {
            return errorResponse(HttpStatus.NOT_FOUND, "Nie znaleziono klienta o ID " + id);
        }

        KlientDTO dto = new KlientDTO(optionalKlient.get());
        return ResponseEntity.ok(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateKlient(@PathVariable("id") Integer id, @Valid @RequestBody Klient klient) {
        if (klient == null) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Brak danych klienta w żądaniu");
        }

        Optional<Klient> optionalKlient = klientRepo.findById(id);
        if (optionalKlient.isEmpty()) {
            return errorResponse(HttpStatus.NOT_FOUND, "Nie znaleziono klienta o ID " + id);
        }

        Klient existing = optionalKlient.get();
        existing.setImie(klient.getImie());
        existing.setNazwisko(klient.getNazwisko());
        existing.setEmail(klient.getEmail());
        existing.setNrTel(klient.getNrTel());

        KlientDTO dto = new KlientDTO(klientRepo.save(existing));
        return ResponseEntity.ok(dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKlient(@PathVariable("id") Integer id) {
        Optional<Klient> klientOpt = klientRepo.findById(id);

        if (klientOpt.isEmpty()) {
            return errorResponse(HttpStatus.NOT_FOUND, "Nie można usunąć – klient o ID " + id + " nie istnieje");
        }

        Klient klient = klientOpt.get();

        if (!klient.getPrzewozy().isEmpty()) {
            List<Integer> idPrzewozow = klient.getPrzewozy().stream()
                    .map(Przewoz::getIdPrzewoz)
                    .toList();

            return errorResponse(HttpStatus.BAD_REQUEST,
                    "Nie można usunąć – klient uczestniczy w przewozach o ID: " + idPrzewozow + ". Należy najpierw usunąć przewóz.");
        }

        klientRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Klient o ID " + id + " został usunięty"));
    }

    // FILTER
    @GetMapping("/szukaj")
    public ResponseEntity<Map<String, List<KlientDTO>>> searchByEmail(@RequestParam("email") String email) {
        List<KlientDTO> lista = klientRepo.findByEmailContainingIgnoreCase(email)
                .stream()
                .map(KlientDTO::new)
                .toList();

        return ResponseEntity.ok(Map.of("klientDTOList", lista));
    }

}
