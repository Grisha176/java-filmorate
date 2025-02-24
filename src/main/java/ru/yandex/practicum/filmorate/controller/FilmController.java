package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody final Film film) {
        validateFilm(film);
        film.setId(getNextId());
        if (films.containsKey(film.getId())) {
            log.warn("Фильм с id {} уже существует", film.getId());
            throw new ValidationException("Такой фильм уже существует");
        }
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody final Film film) {
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            log.error("Фильм с id {} не найден", film.getId());
            throw new ValidationException("Фильм с id:" + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Запрошен список всех фильмов");
        return films.values();
    }

    private void validateFilm(final Film film) {
        LocalDate dateFilm = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(dateFilm)) {
            log.error("Дата релиза раньше 28 декабря 1895 года: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }

    private long getNextId() {
        long id = films.keySet().stream().mapToLong(Long::longValue).max().orElse(0);
        return ++id;
    }
}