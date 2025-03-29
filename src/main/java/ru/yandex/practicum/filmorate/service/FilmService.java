package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.util.Set;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public void addLike(Long filmId, Long userId) {
        validate(filmId, userId);
        filmStorage.addLike(filmId, userId);
        log.info("Добавление лайка");
    }

    public void deleteLike(Long filmId, Long userId) {
        validate(filmId, userId);
        filmStorage.deleteLike(filmId, userId);
        log.info("Успешное даление лайка");
    }

    public Set<Film> getTheMostPopularFilm(int count) {
        log.info("Возвращакем наиболее популярные фильмы");

        return filmStorage.getTheMostPopularFilm(count);
    }

    private void validate(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (user == null) {
            throw new NotFoundException("User с id " + userId + " не найден");
        }
    }
}
