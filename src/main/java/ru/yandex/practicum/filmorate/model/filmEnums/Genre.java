package ru.yandex.practicum.filmorate.model.filmEnums;

import lombok.Getter;

@Getter
public enum Genre {
    COMEDY("Комедия"),
    DRAMA("Драма"),
    HORROR("Ужасы"),
    SCIENCE_FICTION("Фантастика"),
    MELODRAMA("Мелодрама"),
    ACTION("Боевик"),
    THRILLER("Триллер"),
    DETECTIVE("Детектив"),
    ADVENTURE("Приключения"),
    FANTASY("Фэнтези");

    private final String name;

    Genre(String name) {
        this.name = name;
    }

}
