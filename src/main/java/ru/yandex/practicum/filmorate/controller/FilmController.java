package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.ImMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.util.*;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }
    public FilmController(){
        UserStorage userStorage = new InMemoryUserStorage();
        filmStorage = new ImMemoryFilmStorage();
        filmService = new FilmService(filmStorage,userStorage);
    }


    @PostMapping
    public Film addFilm(@Valid @RequestBody  Film film) {
       return filmStorage.addFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<String> addLikeFilm(@PathVariable("id") Long filmId,@PathVariable("userId") Long userId){
        filmService.addLike(filmId,userId);
        return new ResponseEntity<>("{\"message\":\"Добавление лайка прошло успешно\"}", HttpStatus.OK);
    }


    @PutMapping
    public Film updateFilm(@Valid @RequestBody final Film film) {
        return filmStorage.updateFilm(film);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilm(@RequestParam(defaultValue = "10") int count){
        return filmService.getTheMostPopularFilm(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<String> deleteLikeFilm(@PathVariable("id") Long filmId,@PathVariable("userId") Long userId){
        filmService.deleteLike(filmId,userId);
        return new ResponseEntity<>("{\"message\":\"Удаление лайка прошло успешно\"}", HttpStatus.OK);
    }



}