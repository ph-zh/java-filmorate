package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Integer id) {
        return filmService.getById(id);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLikeForDb(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLikeFromDb(id, userId);
    }

    @GetMapping("popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getPopularFromDb(count);
    }
}
