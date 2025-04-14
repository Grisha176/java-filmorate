package ru.yandex.practicum.filmorate.mappers;


import ru.yandex.practicum.filmorate.dto.filmDto.FilmDto;
import ru.yandex.practicum.filmorate.dto.filmDto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.filmDto.UpdatedFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;

public class FilmMapper {

    public static Film mapToFilm(NewFilmRequest request) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        film.setMpa(request.getMpa());
        film.setFilmGenre(request.getGenres());
        return film;
    }

   /* public static FilmDto mapToFilmDto(Film film){
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String filmRating = mapper.writeValueAsString(film.getMpa());
            filmDto.setMpa(filmRating);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return filmDto;
    }*/

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        filmDto.setGenres(film.getFilmGenre());

        FilmRating mpaDto = new FilmRating();
        if (film.getMpa() != null) {
            mpaDto = film.getMpa();
            filmDto.setMpa(mpaDto);
        } else {
            filmDto.setMpa(null); // Если mpa отсутствует, устанавливаем null
        }

        return filmDto;
    }

    public static Film updateFields(Film film, UpdatedFilmRequest request) {
        if (request.hasName()) {
            film.setName(request.getName());
        }
        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }
        if (request.hasReleaseDate()) {
            film.setReleaseDate(request.getReleaseDate());
        }
        if (request.hasDuration()) {
            film.setDuration(request.getDuration());
        }
        if (request.hasRating()) {
            // film.setMpa(request.getMpaRatingId());
        }
        if (request.hasGenre()) {
            film.setFilmGenre(request.getFilmGenre());
        }
        return film;
    }
}

