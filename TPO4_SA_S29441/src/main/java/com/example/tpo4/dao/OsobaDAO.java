package com.example.tpo4.dao;

import com.example.tpo4.models.Osoba;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
                    osoba.setNr_telefonu(rs.getString("NR_TELEFONU"));
                    return osoba;
                }
        );
    }

    public Osoba getById(int id) {
         var obj = jdbcTemplate.queryForObject(
                "SELECT ID, IMIE, NAZWISKO, DATA_URODZENIA, NR_TELEFONU FROM OSOBA WHERE ID = "+id,
                (rs, rowNum) -> {
                    Osoba osoba = new Osoba();
                    osoba.setId(rs.getInt("ID"));
                    osoba.setImie(rs.getString("IMIE"));
                    osoba.setNazwisko(rs.getString("NAZWISKO"));
                    osoba.setData_urodzenia(rs.getDate("DATA_URODZENIA"));
                    osoba.setNr_telefonu(rs.getString("NR_TELEFONU"));
                    return osoba;
                }
        );
//         if (obj != null)
             return obj;
//         else throw new OsobaNotFoundException(id);
    }

    public Osoba save(Osoba osoba) throws IllegalArgumentException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (osoba.getImie() == null)
            throw new IllegalArgumentException("Imie must not be null");
        if (osoba.getNazwisko() == null)
            throw new IllegalArgumentException("Nazwisko must not be null");
        if (osoba.getNr_telefonu() == null)
            throw new IllegalArgumentException("Nr_telefonu must not be null");

        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement("INSERT INTO OSOBA (IMIE, NAZWISKO, DATA_URODZENIA, NR_TELEFONU) VALUES (?,?,?,?)", new String[] { "ID" });
            ps.setString(1, osoba.getImie());
            ps.setString(2, osoba.getNazwisko());
            ps.setDate(3, osoba.getData_urodzenia());
            ps.setString(4, osoba.getNr_telefonu());
            return ps;
        }, keyHolder);

        return getById(keyHolder.getKey().intValue());
    }

    public Osoba update(int id, Osoba osoba) {

        if (osoba.getImie() == null)
            throw new IllegalArgumentException("Imie must not be null");
        if (osoba.getNazwisko() == null)
            throw new IllegalArgumentException("Nazwisko must not be null");
        if (osoba.getNr_telefonu() == null)
            throw new IllegalArgumentException("Nr_telefonu must not be null");

        jdbcTemplate.update("UPDATE OSOBA SET IMIE = ?, NAZWISKO = ?, DATA_URODZENIA = ?, NR_TELEFONU = ? WHERE id = ?",
                osoba.getImie(), osoba.getNazwisko(), osoba.getData_urodzenia(), osoba.getNr_telefonu(), id);
        return getById(id);
    }

    public void delete(int id) {
        getById(id);
        jdbcTemplate.update("DELETE FROM OSOBA WHERE ID = ?", id);
    }
}
