package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.Collection;

@Service
public class GenreService {

    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Genre getGenreById(int idGenre) {
        return genreDao.getGenreById(idGenre);
    }

    public Collection<Genre> findAll() {
        return genreDao.findAll();
    }
}
