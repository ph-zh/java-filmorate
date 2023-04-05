package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {
    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film getById(Integer id);


    void addLikeForDb(Integer id, Integer userId);

    void removeLikeFromDb(Integer id, Integer userId);

    Collection<Film> getPopularFromDb(Integer count);
}
