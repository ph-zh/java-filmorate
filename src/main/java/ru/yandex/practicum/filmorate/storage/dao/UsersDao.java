package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

@Component
@Slf4j
public class UsersDao implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UsersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> findAll() {
        String sql = "select * from users";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User create(User user) {
        String sql = "insert into users(email, login, name, birthday) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        user.setId(id);

        return user;
    }

    @Override
    public User update(User user) {
        checkUserExist(user.getId());

        String sql = "update users set email = ?, login = ?, name = ?, birthday = ? where id = ?";

        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday(), user.getId());

        return user;
    }

    @Override
    public User getById(Integer id) {
        String sql = "select * from users where id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs), id);
        } catch (DataAccessException e) {
            throw new UserNotFoundException(String.format("User with id: %d not found in DB", id));
        }
    }

    @Override
    public void checkUserExist(Integer id) {
        String sql = "select exists (select * from users where id = ?)";

        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, id);

        if (!exists) {
            throw new UserNotFoundException(String.format("User with id: %d not found in DB", id));
        }
    }

    public User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
