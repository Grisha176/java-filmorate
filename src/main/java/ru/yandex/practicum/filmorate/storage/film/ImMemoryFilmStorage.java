package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ImMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
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

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            log.error("Фильм с id {} не найден", film.getId());
            throw new NotFoundException("Фильм с id:" + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Запрошен список всех фильмов");
        return films.values();
    }

    @Override
    public Film getFilmById(Long id) {
        return films.get(id);
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
