package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.LikesDao;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

@Slf4j
@Service
public class FilmService {

    private final LocalDate firstFilmBirthday = LocalDate.of(1895, Month.DECEMBER, 28);

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesDao likesDao;

    @Autowired
    public FilmService(@Qualifier("filmsDao") FilmStorage filmStorage, @Qualifier("usersDao") UserStorage userStorage, LikesDao likesDao) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesDao = likesDao;
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


    public void addLikeForDb(Integer id, Integer userId) {
        likesDao.addLike(id, userId);
    }

    public void removeLikeFromDb(Integer id, Integer userId) {
        likesDao.removeLike(id, userId);
    }

    public Collection<Film> getPopularFromDb(Integer count) {
        return likesDao.getPopular(count);
    }
}
