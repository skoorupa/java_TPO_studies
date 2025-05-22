package com.example.tpo4;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OsobaDAO {
    private final JdbcTemplate jdbcTemplate;

    public OsobaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Osoba> getAll() {
        return jdbcTemplate.query(
                "SELECT ID, IMIE, NAZWISKO, DATA_URODZENIA FROM OSOBA",
                (rs, rowNum) -> {
                    Osoba osoba = new Osoba();
                    osoba.setID(rs.getInt("ID"));
                    osoba.setImie(rs.getString("IMIE"));
                    osoba.setNazwisko(rs.getString("NAZWISKO"));
                    osoba.setData_urodzenia(rs.getDate("DATA_URODZENIA"));
                    return osoba;
                }
        );
    }
}
