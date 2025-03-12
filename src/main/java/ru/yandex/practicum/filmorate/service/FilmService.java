package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.util.List;
import java.util.stream.Collectors;

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
        filmStorage.getFilmById(filmId).getLikesByUsers().add(userId);
        log.info("Добавление лайка");
    }

    public void deleteLike(Long filmId, Long userId) {
        validate(filmId, userId);
        if (!filmStorage.getFilmById(filmId).getLikesByUsers().contains(userId)) {
            log.trace("Пользователь с id" + userId + " не ставил лайк фильму с id " + filmId);
            throw new NotFoundException("Пользователь с id" + userId + " не ставил лайк фильму с id " + filmId);
        }
        log.info("Успешное даление лайка");
        filmStorage.getFilmById(filmId).getLikesByUsers().remove(userId);
    }

    public List<Film> getTheMostPopularFilm(int count) {
        log.info("Возвращакем наиболее популярные фильмы");
        if (filmStorage.getAllFilms() == null) {
            log.trace("Фильмы не найдены");
            throw new NotFoundException("Не удалось найти фильмы");
        }
        return filmStorage.getAllFilms().stream()
                .sorted((film1, film2) -> Long.compare(film2.getLikesByUsers().size(), film1.getLikesByUsers().size())) // Сортировка по убыванию
                .limit(count)
                .collect(Collectors.toList());
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
