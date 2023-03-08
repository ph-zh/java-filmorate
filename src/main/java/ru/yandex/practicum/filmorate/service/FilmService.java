package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final LocalDate firstFilmBirthday = LocalDate.of(1895, Month.DECEMBER, 28);
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private void validFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.warn("Попытка создания фильма с пустым названием");
            throw new ValidationException("Название фильма не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.warn("Попытка создания фильма с описанием свыше 200 знаков");
            throw new ValidationException("Описание фильма превышает максимальное количество знаков 200");
        } else if (film.getReleaseDate().isBefore(firstFilmBirthday)) {
            log.warn("Попытка создания фильма с датой, предшествующей появлению первого фильма");
            throw new ValidationException("Дата релиза фильма введена неверна");
        } else if (film.getDuration() <= 0) {
            log.warn("Попытка создания фильма с отрицательной продолжительностью");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        validFilm(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validFilm(film);
        return filmStorage.update(film);
    }

    public Film getById(Integer id) {
        return filmStorage.getById(id);
    }

    public void addLike(Integer id, Integer userId) {
        Film film = filmStorage.getById(id);

        userStorage.checkUserExist(userId);

        film.getLikesByUsers().add(userId);
    }

    public void removeLike(Integer id, Integer userId) {
        Film film = filmStorage.getById(id);

        userStorage.checkUserExist(userId);

        film.getLikesByUsers().removeIf(integer -> integer.equals(userId));
    }

    public Collection<Film> getPopular(Integer count) {
        return filmStorage.findAll().stream().sorted((f1, f2) -> {
            Integer film1 = f1.getLikesByUsers().size();
            Integer film2 = f2.getLikesByUsers().size();
            return film1.compareTo(film2) * -1;
        }).limit(count).collect(Collectors.toList());
    }
}
