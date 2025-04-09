package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.filmEnums.Genre;

@Data
public class FilmGenre {
    private Long id;
    private Genre genre;
}
