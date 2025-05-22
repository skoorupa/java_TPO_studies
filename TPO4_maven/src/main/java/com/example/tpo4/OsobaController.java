package com.example.tpo4;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/data")
    public ResponseEntity<Osoba> create(@RequestBody Osoba osoba) {
        Osoba saved = osobaDAO.save(osoba);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/data/{id}")
    public ResponseEntity<Osoba> update(@PathVariable int id, @RequestBody Osoba osoba) {
        osoba.setId(id);
        Osoba updated = osobaDAO.update(osoba);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/data/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        osobaDAO.delete(id);
        return ResponseEntity.noContent().build();
    }
}
