package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.MPADao;

import java.util.Collection;

@Service
public class MPAServiceImpl {

    private final MPADao mpaDao;

    public MPAServiceImpl(MPADao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Collection<MPA> findAll() {
        return mpaDao.findAll();
    }

    public MPA getMpaById(int idMPA) {
        return mpaDao.getMpaById(idMPA);
    }
}
