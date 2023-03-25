package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        if (users.containsKey(user.getId())) {
            log.warn("Попытка создать пользователя с уже существующим id");
            throw new ValidationException(String.format("Пользователь с id: %d уже существует", user.getId()));
        }

        user.setId(++id);
        users.put(user.getId(), user);

        log.debug("Пользователь создан: {}", user);

        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Попытка изменить пользователя с не существующим id");
            throw new UserNotFoundException(String.format("Пользователя с id: %d не существует", user.getId()));
        }

        users.put(user.getId(), user);
        log.debug("Пользователь изменён: {}", user);

        return user;
    }

    @Override
    public User getById(Integer id) {
        if (!users.containsKey(id)) {
            log.warn("Попытка получить пользователя с не существующим id");
            throw new UserNotFoundException(String.format("Пользователя с id: %d не существует", id));
        }
        return users.get(id);
    }

    @Override
    public void checkUserExist(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("Пользователя с id: %d не существует", id));
        }
    }
}
