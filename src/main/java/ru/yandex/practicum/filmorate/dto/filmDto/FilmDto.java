package ru.yandex.practicum.filmorate.dto.filmDto;


import lombok.Data;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.time.LocalDate;

@Data
public class FilmDto {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private FilmRating mpa;


}
