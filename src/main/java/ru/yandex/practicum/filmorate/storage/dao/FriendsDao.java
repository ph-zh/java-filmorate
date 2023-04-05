package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Slf4j
@Component
public class FriendsDao {

    private final JdbcTemplate jdbcTemplate;
    private final UsersDao userStorage;

    public FriendsDao(JdbcTemplate jdbcTemplate, UsersDao userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    public void addFriend(Integer id, Integer friendId) {
        userStorage.checkUserExist(id);
        userStorage.checkUserExist(friendId);

        String sql = "insert into friends(id_user, id_friend) values (?, ?)";
        jdbcTemplate.update(sql, id, friendId);
        log.info("Added friendship for user with id: {} from other user with id: {}", id, friendId);
    }

    public void removeFriend(Integer id, Integer idUser) {
        userStorage.checkUserExist(id);
        userStorage.checkUserExist(idUser);

        String sql = "delete from friends where id_user = ? and id_friend = ?";
        jdbcTemplate.update(sql, id, idUser);
        log.info("Friendship of user with id: {} and other user with id: {} removed", id, idUser);
    }

    public Collection<User> getFriends(Integer id) {
        userStorage.checkUserExist(id);

        String sql = "select * from users u, friends f where f.id_user = ? and u.id = f.id_friend";

        return jdbcTemplate.query(sql, (rs, rowNum) -> userStorage.makeUser(rs), id);
    }

    public Collection<User> getCommonFriends(Integer id, Integer otherId) {
        userStorage.checkUserExist(id);
        userStorage.checkUserExist(otherId);

        String sql = "select * from users u, friends f, friends o where u.id = f.id_friend and u.id = o.id_friend " +
                "and f.id_user = ? and o.id_user = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> userStorage.makeUser(rs),
                id, otherId);
    }
}
