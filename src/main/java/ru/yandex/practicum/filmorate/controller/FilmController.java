package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.dto.filmDto.FilmDto;
import ru.yandex.practicum.filmorate.dto.filmDto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.filmDto.UpdatedFilmRequest;

import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequestMapping
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @PostMapping("/films")
    public FilmDto addFilm(@Valid @RequestBody NewFilmRequest film) {
        return filmService.addFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public ResponseEntity<String> addLikeFilm(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        filmService.addLike(filmId, userId);
        return new ResponseEntity<>("{\"message\":\"Добавление лайка прошло успешно\"}", HttpStatus.OK);
    }

    @PutMapping("/films")
    public FilmDto updateFilm(@Valid @RequestBody UpdatedFilmRequest film) {
        return filmService.updateFilm(film.getId(),film);
    }

  /*  @PutMapping("/films/{id}")
    public FilmDto updateFilm(@PathVariable("id") Long filmId, @Valid @RequestBody UpdatedFilmRequest film) {
        return filmService.updateFilm(filmId,film);
    }*/

    @GetMapping("/films")
    public List<FilmDto> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/films/popular")
    public List<FilmDto> getMostPopularFilm(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTheMostPopularFilm(count);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public ResponseEntity<String> deleteLikeFilm(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        filmService.deleteLike(filmId, userId);
        return new ResponseEntity<>("{\"message\":\"Удаление лайка прошло успешно\"}", HttpStatus.OK);
    }

    @GetMapping("/films/{id}")
    public FilmDto getFilmsByGenreId(@PathVariable Long id){
        return filmService.getFilmById(id);
    }

    @GetMapping("/genres")
    public List<FilmGenre> getAllFilmGenre(){
        return filmService.getAllFilmGenre();
    }

    @GetMapping("/genres/{id}")
    public FilmGenre getFilmGereById(@PathVariable int id){
        return filmService.getFilmGenreById(id);
    }

    @GetMapping("/mpa")
    public List<FilmRating> getAllFilmRating(){
        return filmService.getAllRating();
    }

    @GetMapping("/mpa/{id}")
    public FilmRating getFilmRatingById(@PathVariable int id){
        return filmService.getRatingById(id);
    }






}