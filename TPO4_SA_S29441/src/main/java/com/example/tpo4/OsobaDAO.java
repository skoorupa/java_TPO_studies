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
                "SELECT ID, IMIE, NAZWISKO, DATA_URODZENIA, NR_TELEFONU FROM OSOBA ORDER BY ID",
                (rs, rowNum) -> {
                    Osoba osoba = new Osoba();
                    osoba.setId(rs.getInt("ID"));
                    osoba.setImie(rs.getString("IMIE"));
                    osoba.setNazwisko(rs.getString("NAZWISKO"));
                    osoba.setData_urodzenia(rs.getDate("DATA_URODZENIA"));
                    osoba.setNumer_telefonu(rs.getString("NR_TELEFONU"));
                    return osoba;
                }
        );
    }

    public Osoba getById(int id) {
        return jdbcTemplate.queryForObject(
                "SELECT ID, IMIE, NAZWISKO, DATA_URODZENIA, NR_TELEFONU FROM OSOBA WHERE ID = "+id,
                (rs, rowNum) -> {
                    Osoba osoba = new Osoba();
                    osoba.setId(rs.getInt("ID"));
                    osoba.setImie(rs.getString("IMIE"));
                    osoba.setNazwisko(rs.getString("NAZWISKO"));
                    osoba.setData_urodzenia(rs.getDate("DATA_URODZENIA"));
                    osoba.setNumer_telefonu(rs.getString("NR_TELEFONU"));
                    return osoba;
                }
        );
    }

    public Osoba save(Osoba osoba) {
        jdbcTemplate.update(
                "INSERT INTO OSOBA (ID, IMIE, NAZWISKO, DATA_URODZENIA, NR_TELEFONU) VALUES (?,?,?,?,?)",
                osoba.getId(), osoba.getImie(), osoba.getNazwisko(), osoba.getData_urodzenia(), osoba.getNumer_telefonu()
        );
        return getById(osoba.getId());
    }

    public Osoba update(int id, Osoba osoba) {
        jdbcTemplate.update("UPDATE OSOBA SET ID = ?, IMIE = ?, NAZWISKO = ?, DATA_URODZENIA = ?, NR_TELEFONU = ? WHERE id = ?",
                osoba.getId(), osoba.getImie(), osoba.getNazwisko(), osoba.getData_urodzenia(), osoba.getNumer_telefonu(), id);
        return getById(osoba.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM OSOBA WHERE ID = ?", id);
    }
}
