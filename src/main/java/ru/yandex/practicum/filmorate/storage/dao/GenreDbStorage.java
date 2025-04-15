package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseDbStorage<FilmGenre> {

    private static final String GET_ALL_GENRE_QUERY = "SELECT * FROM genre";
    private static final String GET_GENRE_BY_ID_QUERY = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String GET_FILM_GENRES = "SELECT g.genre_id, g.genre " +
                    "FROM film_genre AS fg " +
                    "JOIN genre AS g ON fg.genre_id = g.genre_id " +
                    "WHERE fg.film_id = ?";


    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public Optional<FilmGenre> getFilmGenreById(int id) {
        return findOne(GET_GENRE_BY_ID_QUERY, id);
    }

    public List<FilmGenre> getAllGenre() {
        return findMany(GET_ALL_GENRE_QUERY);
    }

    public List<FilmGenre> getFilmGenre(Long filmId) {
        return findMany(GET_FILM_GENRES, filmId);
    }

}
