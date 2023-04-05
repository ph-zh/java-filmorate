package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

public interface MPAService {

    Collection<MPA> findAll();

    MPA getMpaById(int idMPA);
}
