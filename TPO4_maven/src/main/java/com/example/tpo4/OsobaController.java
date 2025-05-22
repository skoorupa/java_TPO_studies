package com.example.tpo4;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class OsobaController {
    private final OsobaDAO osobaDAO;

    public OsobaController(OsobaDAO osobaDAO) {
        this.osobaDAO = osobaDAO;
    }

    @GetMapping("/data")
    public List<Osoba> getAll() {
        return osobaDAO.getAll();
    }
}
