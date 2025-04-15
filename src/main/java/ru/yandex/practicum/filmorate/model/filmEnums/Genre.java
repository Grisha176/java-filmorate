package ru.yandex.practicum.filmorate.model.filmEnums;

import lombok.Getter;

import java.util.Optional;

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

    public static Optional<Genre> fromName(String name) {
        for (Genre genre : values()) {
            if (genre.getName().equalsIgnoreCase(name)) {
                return Optional.of(genre);
            }
        }
        return Optional.empty();
    }

}
