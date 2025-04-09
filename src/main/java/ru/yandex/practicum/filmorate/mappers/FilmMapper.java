package ru.yandex.practicum.filmorate.mappers;


import ru.yandex.practicum.filmorate.dto.filmDto.FilmDto;
import ru.yandex.practicum.filmorate.dto.filmDto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.filmDto.UpdatedFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;

public class FilmMapper {

    public static Film mapToFilm(NewFilmRequest request){
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        film.setMpaRatingId(request.getMpaRatingId());
        return film;
    }

    public static FilmDto mapToFilmDto(Film film){
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        filmDto.setMpa(new FilmRating(film.getMpaRatingId()));
        return filmDto;
    }

    public static Film updateFields(Film film, UpdatedFilmRequest request){
        if(request.hasName()){
            film.setName(request.getName());
        }
        if(request.hasDescription()){
            film.setDescription(request.getDescription());
        }
        if(request.hasReleaseDate()){
            film.setReleaseDate(request.getReleaseDate());
        }
        if(request.hasDuration()){
            film.setDuration(request.getDuration());
        }
        if(request.hasRating()){
            film.setMpaRatingId(request.getMpaRatingId());
        }
        return film;
    }
}

