package ru.yandex.practicum.filmorate.dto.filmDto;


import lombok.Data;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.model.filmEnums.Genre;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class FilmDto {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private FilmRating mpa;
    private Set<FilmGenre> genres = new HashSet<>();

}
