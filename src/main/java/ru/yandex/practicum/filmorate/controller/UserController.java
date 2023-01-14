package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        if (users.containsKey(user.getId())) {
            log.warn("Попытка создать пользователя с уже существующим id");
            throw new ValidationException("Пользователь с таким id уже существует");
        }
        validUser(user);

        user.setId(++id);
        users.put(user.getId(), user);

        log.debug("Пользователь создан: {}", user);

        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        if (users.containsKey(user.getId())) {
            validUser(user);
            users.put(user.getId(), user);
            log.debug("Пользователь изменён: {}", user);
        } else {
            log.warn("Попытка изменить пользователя с не существующим id");
            throw new ValidationException("Пользователь с таким id ещё не создан");
        }

        return user;
    }

    private void validUser(User user) {
        LocalDate now = LocalDate.now();

        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank()
                || !user.getEmail().contains("@")) {
            log.warn("Попытка создать пользователя с пустым или не корректным адресом email");
            throw new ValidationException("Адрес почты введён неверно");
        } else if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank()
                || user.getLogin().contains(" ")) {
            log.warn("Попытка создать пользователя с пустым или содержащим пробелы логином");
            throw new ValidationException("Логин не должен быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(now)) {
            log.warn("Попытка создать пользователя с датой рождения из будущего");
            throw new ValidationException("День рождения не может быть из будущего :)");
        }

        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.warn("Попытка создать пользователя с пустым именем, вместо имени будет присвоен логин");
            user.setName(user.getLogin());
        }
    }
}
