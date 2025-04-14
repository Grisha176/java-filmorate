package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmGenre {
    private Long id;
    private String name;

    public FilmGenre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public FilmGenre() {

    }
}
