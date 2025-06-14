package com.example.demo;

import com.example.demo.model.Autobus;
import com.example.demo.model.Klient;
import com.example.demo.model.Przewoz;
import com.example.demo.model.Trasa;
import com.example.demo.repository.AutobusRepository;
import com.example.demo.repository.KlientRepository;
import com.example.demo.repository.PrzewozRepository;
import com.example.demo.repository.TrasaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class SampleDataLoader {

    @Value("${app.sampledata.enabled:true}")
    private boolean sampleDataEnabled;

    @Bean
    public CommandLineRunner loadSampleData(AutobusRepository autobusRepo, KlientRepository klientRepo, TrasaRepository trasaRepo, PrzewozRepository przewozRepo) {
        return args -> {
            if (!sampleDataEnabled) return;

            // AUTOBUSY
            Autobus a1 = new Autobus(null, "Mercedes", "Sprinter", "KR1234A", 20);
            Autobus a2 = new Autobus(null, "Setra", "S415", "KR5678B", 50);
            Autobus a3 = new Autobus(null, "Solaris", "15", "KR9012C", 30);
            Autobus a4 = new Autobus(null, "Solaris", "Urbino 15", "KR3456D", 55);
            autobusRepo.saveAll(List.of(a1, a2, a3, a4));

            // KLIENCI
            Klient k1 = new Klient(null, "Jan", "Kowalski", "jan.kowalski@example.com", "123456789", null);
            Klient k2 = new Klient(null, "Anna", "Nowak", "anna.nowak@example.com", "234567891", null);
            Klient k3 = new Klient(null, "Piotr", "Zielinski", "piotr.zielinski@example.com", "345678912", null);
            Klient k4 = new Klient(null, "Maria", "Wiśniewska", "maria.wisniewska@example.com", "456789123", null);
            klientRepo.saveAll(List.of(k1, k2, k3, k4));

            // TRASY
            Trasa t1 = new Trasa(null, "Kraków", "Warszawa", 300, 240);
            Trasa t2 = new Trasa(null, "Kraków", "Wrocław", 270, 210);
            Trasa t3 = new Trasa(null, "Kraków", "Gdańsk", 500, 420);
            Trasa t4 = new Trasa(null, "Kraków", "Poznań", 400, 330);
            trasaRepo.saveAll(List.of(t1, t2, t3, t4));

            // PRZEWOZY (powiązania)
            Przewoz p1 = new Przewoz(null, t1, a1, LocalDate.now().plusDays(1), LocalDate.now().plusDays(1), 49.99, List.of(k1));
            Przewoz p2 = new Przewoz(null, t2, a2, LocalDate.now().plusDays(2), LocalDate.now().plusDays(2), 59.99, List.of(k2));
            Przewoz p3 = new Przewoz(null, t3, a3, LocalDate.now().plusDays(3), LocalDate.now().plusDays(3), 69.99, List.of(k3));
            Przewoz p4 = new Przewoz(null, t4, a4, LocalDate.now().plusDays(4), LocalDate.now().plusDays(4), 79.99, List.of(k1, k2, k3, k4));
            przewozRepo.saveAll(List.of(p1, p2, p3, p4));
        };
    }
}
