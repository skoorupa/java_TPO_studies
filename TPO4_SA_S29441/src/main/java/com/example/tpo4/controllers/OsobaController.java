package com.example.tpo4.controllers;

import com.example.tpo4.exceptions.OsobaNotFoundException;
import com.example.tpo4.models.Osoba;
import com.example.tpo4.dao.OsobaDAO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/data/{id}")
    public ResponseEntity<Osoba> getById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(osobaDAO.getById(id));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/data")
    public ResponseEntity<Osoba> create(@RequestBody Osoba osoba) {
        try {
            Osoba saved = osobaDAO.save(osoba);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/data/{id}")
    public ResponseEntity<Osoba> update(@PathVariable int id, @RequestBody Osoba osoba) {
        try {
            Osoba updated = osobaDAO.update(id, osoba);
            return ResponseEntity.ok(updated);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/data/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        try {
            osobaDAO.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
