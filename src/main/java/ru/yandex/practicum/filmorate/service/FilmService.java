package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.filmDto.FilmDto;
import ru.yandex.practicum.filmorate.dto.filmDto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.filmDto.UpdatedFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.RatingDbStorage;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;
    private final GenreDbStorage genreStorage;
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;
    private final RatingDbStorage ratingStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, GenreDbStorage genreStorage, UserStorage userStorage, RatingDbStorage ratingStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.userStorage = userStorage;
        this.ratingStorage = ratingStorage;
    }


    public FilmDto addFilm(NewFilmRequest request) {
        Film film = FilmMapper.mapToFilm(request);
        for (FilmGenre filmGenre : film.getFilmGenre()) {
            genreStorage.getFilmGenreById(filmGenre.getId().intValue()).orElseThrow(() -> new NotFoundException("Жанр с id:" + filmGenre.getId() + " не найден"));
        }

        film = filmStorage.addFilm(film);
        return FilmMapper.mapToFilmDto(film);
    }

    public void addLike(Long filmId, Long userId) {
        validate(filmId, userId);
        filmStorage.addLike(filmId, userId);
        log.info("Добавление лайка");
    }


    public FilmDto updateFilm(Long filmId, UpdatedFilmRequest request) {
        Film film = filmStorage.getFilmById(filmId).orElseThrow(() -> new NotFoundException("Фильм с id:" + filmId + " не найден"));
        film = FilmMapper.updateFields(film, request);
        film = filmStorage.updateFilm(film);
        return FilmMapper.mapToFilmDto(film);
    }


    public FilmGenre getFilmGenreById(int genreId) {
        return genreStorage.getFilmGenreById(genreId).orElseThrow(() -> new NotFoundException("Genre с id:" + genreId + " не найден"));
    }

    public List<FilmGenre> getAllFilmGenre() {
        return genreStorage.getAllGenre();
    }

    public List<FilmDto> getAllFilms() {
        return filmStorage.getAllFilms().stream().map(FilmMapper::mapToFilmDto).collect(Collectors.toList());
    }

    public List<FilmDto> getFilmsByGenreId(int id) {
        return filmStorage.getFilmsByGenreId(id).stream().map(FilmMapper::mapToFilmDto).collect(Collectors.toList());
    }

    public FilmDto getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id).orElseThrow(() -> new NotFoundException("Фильм с id:" + id + " не найден"));
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmRating getRatingById(int id) {
        return ratingStorage.getFilmRating(id).orElseThrow(() -> new NotFoundException("Rating с id:" + id + " не найден"));
    }

    public List<FilmRating> getAllRating() {
        return ratingStorage.getAllRating();
    }


    public List<FilmDto> getTheMostPopularFilm(int count) {
        log.info("Возвращакем наиболее популярные фильмы");
        return filmStorage.getTheMostPopularFilm(count).stream().map(FilmMapper::mapToFilmDto).collect(Collectors.toList());
    }

    public void deleteLike(Long filmId, Long userId) {
        validate(filmId, userId);
        filmStorage.deleteLike(filmId, userId);
        log.info("Успешное удаление лайка");
    }

    private void validate(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId).orElseThrow(() -> new NotFoundException("Фильм с id " + filmId + " не найден"));
        User user = userStorage.getUserById(userId).orElseThrow(() -> new NotFoundException("User с id " + userId + " не найден"));
    }
}
