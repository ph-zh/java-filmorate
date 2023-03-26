package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class MPA {

    private int id;
    private String name;
    private String description;

    public MPA(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
