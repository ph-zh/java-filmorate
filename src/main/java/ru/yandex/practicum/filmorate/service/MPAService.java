package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.MPADao;

import java.util.Collection;

@Service
public class MPAService {

    private final MPADao mpaDao;

    public MPAService(MPADao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Collection<MPA> findAll() {
        return mpaDao.findAll();
    }

    public MPA getMpaById(int idMPA) {
        return mpaDao.getMpaById(idMPA);
    }
}
