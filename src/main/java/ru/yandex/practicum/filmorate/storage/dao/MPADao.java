package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

@Component
public class MPADao {

    private final JdbcTemplate jdbcTemplate;

    public MPADao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MPA getMpaById(int idMPA) {
        String sql = "select id, name, description from mpa where id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new MPA(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description")), idMPA);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format("Rating MPA with id: %d not found in DB", idMPA));
        }
    }

    public Collection<MPA> findAll() {
        String sql = "select * from mpa";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new MPA(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description")));
    }
}
