package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private MPA mpa;
    private List<Genre> genres;
}
