package com.example.demo.controller;

import com.example.demo.model.Autobus;
import com.example.demo.model.Klient;
import com.example.demo.model.Przewoz;
import com.example.demo.model.Trasa;
import com.example.demo.model.dto.KlientDTO;
import com.example.demo.model.dto.PrzewozCreateDTO;
import com.example.demo.model.dto.PrzewozDTO;
import com.example.demo.repository.AutobusRepository;
import com.example.demo.repository.KlientRepository;
import com.example.demo.repository.PrzewozRepository;
import com.example.demo.repository.TrasaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/przewozy")
@RequiredArgsConstructor
public class PrzewozController {

    private final PrzewozRepository przewozRepo;
    private final KlientRepository klientRepo;
    private final AutobusRepository autobusRepo;
    private final TrasaRepository trasaRepo;

    private ResponseEntity<?> message(HttpStatus status, String msg) {
        return ResponseEntity.status(status).body(Map.of("message", msg));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> addPrzewoz(@Valid @RequestBody PrzewozCreateDTO dto) {
        if (dto.getDataWyjazdu().isAfter(dto.getDataPrzyjazdu())) {
            return message(HttpStatus.BAD_REQUEST, "Data wyjazdu nie może być po dacie przyjazdu.");
        }

        Autobus autobus = autobusRepo.findById(dto.getIdAutobus())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autobus o podanym ID nie istnieje"));

        Trasa trasa = trasaRepo.findById(dto.getIdTrasa())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trasa o podanym ID nie istnieje"));

        Przewoz przewoz = new Przewoz();
        przewoz.setDataWyjazdu(dto.getDataWyjazdu());
        przewoz.setDataPrzyjazdu(dto.getDataPrzyjazdu());
        przewoz.setCena(dto.getCena());
        przewoz.setAutobus(autobus);
        przewoz.setTrasa(trasa);

        Przewoz zapisany = przewozRepo.save(przewoz);
        return ResponseEntity.ok(new PrzewozDTO(zapisany));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<PrzewozDTO> lista = StreamSupport.stream(przewozRepo.findAll().spliterator(), false)
                .map(PrzewozDTO::new)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return message(HttpStatus.NOT_FOUND, "Brak przewozów w bazie");
        }

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getPrzewoz(@PathVariable("id") Integer id) {
        Optional<Przewoz> przewozOpt = przewozRepo.findById(id);

        return przewozOpt
                .<ResponseEntity<?>>map(przewoz -> ResponseEntity.ok(new PrzewozDTO(przewoz)))
                .orElseGet(() -> message(HttpStatus.NOT_FOUND, "Nie znaleziono przewozu o ID " + id));
    }

    // UPDATE using PrzewozCreateDTO
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrzewoz(@PathVariable("id") Integer id, @Valid @RequestBody PrzewozCreateDTO dto) {
        Optional<Przewoz> opt = przewozRepo.findById(id);
        if (opt.isEmpty()) {
            return message(HttpStatus.NOT_FOUND, "Nie znaleziono przewozu o ID " + id);
        }

        if (dto.getDataWyjazdu().isAfter(dto.getDataPrzyjazdu())) {
            return message(HttpStatus.BAD_REQUEST, "Data wyjazdu nie może być po dacie przyjazdu.");
        }

        Autobus autobus = autobusRepo.findById(dto.getIdAutobus())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autobus o podanym ID nie istnieje"));

        Trasa trasa = trasaRepo.findById(dto.getIdTrasa())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trasa o podanym ID nie istnieje"));

        Przewoz existing = opt.get();
        existing.setDataWyjazdu(dto.getDataWyjazdu());
        existing.setDataPrzyjazdu(dto.getDataPrzyjazdu());
        existing.setCena(dto.getCena());
        existing.setAutobus(autobus);
        existing.setTrasa(trasa);

        return ResponseEntity.ok(new PrzewozDTO(przewozRepo.save(existing)));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrzewoz(@PathVariable("id") Integer id) {
        if (!przewozRepo.existsById(id)) {
            return message(HttpStatus.NOT_FOUND, "Nie można usunąć – przewóz o ID " + id + " nie istnieje");
        }
        przewozRepo.deleteById(id);
        return message(HttpStatus.OK, "Przewóz o ID " + id + " został usunięty");
    }

    // GET CLIENTS
    @GetMapping("/{id}/klienci")
    public ResponseEntity<?> getKlienciForPrzewoz(@PathVariable("id") Integer id) {
        Optional<Przewoz> przewozOpt = przewozRepo.findById(id);

        if (przewozOpt.isEmpty()) {
            return message(HttpStatus.NOT_FOUND, "Nie znaleziono przewozu o ID " + id);
        }

        List<KlientDTO> lista = przewozOpt.get().getKlienci().stream()
                .map(KlientDTO::new)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    // ADD CLIENT
    @PostMapping("/{idPrzewozu}/dodaj-klienta/{idKlienta}")
    public ResponseEntity<?> dodajKlientaDoPrzewozu(@PathVariable("idPrzewozu") Integer idPrzewozu,
                                                    @PathVariable("idKlienta") Integer idKlienta) {

        Optional<Przewoz> przewozOpt = przewozRepo.findById(idPrzewozu);
        if (przewozOpt.isEmpty()) {
            return message(HttpStatus.NOT_FOUND, "Nie znaleziono przewozu o ID " + idPrzewozu);
        }

        Optional<Klient> klientOpt = klientRepo.findById(idKlienta);
        if (klientOpt.isEmpty()) {
            return message(HttpStatus.NOT_FOUND, "Nie znaleziono klienta o ID " + idKlienta);
        }

        Przewoz przewoz = przewozOpt.get();
        Klient klient = klientOpt.get();

        if (przewoz.getKlienci().contains(klient)) {
            return message(HttpStatus.BAD_REQUEST, "Klient jest już zapisany na ten przewóz.");
        }

        int maxMiejsc = przewoz.getAutobus().getLiczbaMiejsc();
        if (przewoz.getKlienci().size() >= maxMiejsc) {
            return message(HttpStatus.BAD_REQUEST, "Brak dostępnych miejsc w autobusie.");
        }

        przewoz.getKlienci().add(klient);
        klient.getPrzewozy().add(przewoz);

        return ResponseEntity.ok(new PrzewozDTO(przewozRepo.save(przewoz)));
    }

    // FILTER
    @GetMapping("/szukaj")
    public ResponseEntity<?> filterByCena(@RequestParam("cenaMin") Double cenaMin,
                                          @RequestParam("cenaMax") Double cenaMax) {

        List<PrzewozDTO> lista = przewozRepo.findByCenaBetween(cenaMin, cenaMax)
                .stream()
                .map(PrzewozDTO::new)
                .toList();

        if (lista.isEmpty()) {
            return message(HttpStatus.NOT_FOUND, "Brak przewozów w podanym zakresie cenowym");
        }

        return ResponseEntity.ok(CollectionModel.of(lista));
    }
}
