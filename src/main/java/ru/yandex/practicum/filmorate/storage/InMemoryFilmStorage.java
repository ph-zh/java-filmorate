package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        if (films.containsKey(film.getId())) {
            log.warn("Попытка создать фильм с уже существующим id");
            throw new ValidationException("Фильм с таким id уже существует");
        }

        film.setId(++id);
        films.put(film.getId(), film);

        log.debug("Создан новый фильм: {}", film);

        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Попытка изменить фильм по не существующему id");
            throw new FilmNotFoundException("Фильма с таким id не существует, обновление невозможно");
        }

        films.put(film.getId(), film);
        log.debug("Внесены изменения в фильм: {}", film);

        return film;
    }

    @Override
    public Film getById(Integer id) {
        if (!films.containsKey(id)) {
            log.warn("Попытка получить фильм по несуществующему id");
            throw new FilmNotFoundException(String.format("Фильма с id: %d не существует, получение невозможно", id));
        }

        return films.get(id);
    }


}
