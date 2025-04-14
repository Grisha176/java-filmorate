package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Repository("FilmDbStorage")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {

    private final GenreDbStorage genreDbStorage;
    private final RatingDbStorage ratingDbStorage;

    private static final String INSERT_INTO_QUERY = "INSERT INTO films(name,description,release_date,duration,mpa_rating_id) " + "VALUES(?,?,?,?,?)";
    private static final String INSERT_INTO_LIKE_QUERY = "INSERT INTO film_likes(film_id,user_id) VALUES(?,?)";
    private static final String INSERT_INTO_FILM_GENRE_QUERY = "INSERT INTO film_genre(film_id,genre_id) VALUES(?,?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?,description = ?,duration = ?,release_date = ?,mpa_rating_id = ? WHERE film_id = ?";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM film_likes WHERE user_id = ? AND film_id = ?";
    private static final String GET_THE_MOST_POPULAR_QUERY =
            "SELECT f.film_id, f.name, f.description, f.duration, f.release_date, f.mpa_rating_id, COUNT(fl.user_id) AS likes " +
                    "FROM films AS f " +
                    "LEFT JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                    "GROUP BY f.film_id, f.name,f.description, f.duration, f.release_date, f.mpa_rating_id " +
                    "ORDER BY likes DESC " +
                    "LIMIT ?";
    private static final String GET_FILM_LIKES_QUERY = "SELECT * FROM film_likes WHERE film_id = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM films";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String GET_GENRE_QUERY =
            "SELECT f.* " +
                    "FROM films AS f " +
                    "JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                    "WHERE fg.genre_id = ?";


    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper, GenreDbStorage genreDbStorage, RatingDbStorage ratingDbStorage) {
        super(jdbc, mapper);
        this.genreDbStorage = genreDbStorage;
        this.ratingDbStorage = ratingDbStorage;
    }

    @Override
    public Film addFilm(Film film) {
        FilmRating mpa = ratingDbStorage.getFilmRating(film.getMpa().getId()).orElseThrow(() -> new NotFoundException("Mpa c id:" + film.getMpa().getId() + " не найден"));
        String mpaName = ratingDbStorage.getFilmRatingName(mpa.getId());
        mpa.setName(mpaName);
        film.setMpa(mpa);

        long id = insert(INSERT_INTO_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                mpa.getId()
        );
        film.setId(id);
        if (!hasGenre(film)) {
            throw new NotFoundException("Неверный жанр айди");
        }
        return film;

    }

    @Override
    public Film updateFilm(Film film) {
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return findMany(GET_ALL_QUERY);
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return findOne(GET_BY_ID_QUERY, id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        insert(INSERT_INTO_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        delete(DELETE_LIKE_QUERY, userId, filmId);
    }

    @Override
    public List<Film> getTheMostPopularFilm(int count) {
        return findMany(GET_THE_MOST_POPULAR_QUERY, count);
    }

    private boolean hasGenre(Film film) {
        Set<Long> allGenreIds = genreDbStorage.getAllGenre().stream()
                .map(FilmGenre::getId)
                .collect(Collectors.toSet());
        Set<Long> filmGenreIds = film.getFilmGenre().stream()
                .map(FilmGenre::getId)
                .collect(Collectors.toSet());
        if (!filmGenreIds.isEmpty()) {
            for (Long genreId : filmGenreIds) {
                insert(INSERT_INTO_FILM_GENRE_QUERY, film.getId(), genreId);
            }
            return true;
        }
        return allGenreIds.containsAll(filmGenreIds);
    }

    public List<Film> getFilmsByGenreId(int id) {
        return findMany(GET_GENRE_QUERY, id);
    }


}
