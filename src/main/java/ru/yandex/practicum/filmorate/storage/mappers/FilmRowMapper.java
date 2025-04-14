package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.storage.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.RatingDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.stream.Collectors;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Autowired
    private RatingDbStorage ratingDbStorage;
    @Autowired
    private GenreDbStorage genreDbStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getInt("duration"));

        Integer mpaId = rs.getInt("mpa_rating_id");
        String mpaName = ratingDbStorage.getFilmRatingName(mpaId);
        film.setMpa(new FilmRating(mpaId, mpaName));

        film.setFilmGenre(genreDbStorage.getFilmGenre(film.getId()).stream().collect(Collectors.toSet()));


        Timestamp timestamp = rs.getTimestamp("release_date");
        film.setReleaseDate(timestamp.toLocalDateTime().toLocalDate());
        return film;
    }
}
