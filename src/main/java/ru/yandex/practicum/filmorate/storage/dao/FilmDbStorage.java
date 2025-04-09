package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Repository("FilmDbStorage")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage  {

     private static final String INSERT_INTO_QUERY = "INSERT INTO films(name,description,release_date,duration,mpa_rating_id) "+"VALUES(?,?,?,?,?)";
     private static final String INSERT_INTO_LIKE_QUERY = "INSERT INTO film_likes(film_id,user_id) VALUES(?,?)";
     private static final String INSERT_INTO_FILM_GENRE_QUERY = "INSERT INTO film_genre(film_id,genre_id) VALUES(?,?)";
     private static final String UPDATE_QUERY = "UPDATE films SET name = ?,description = ?,duration = ?,release_date = ?,mpa_rating_id = ? WHERE film_id = ?";
     private static final String DELETE_LIKE_QUERY = "DELETE FROM film_likes WHERE user_id = ? AND film_id = ?";
     private static final String GET_THE_MOST_POPULAR_QUERY =
            "SELECT f.film_id, f.name, COUNT(fl.user_id) AS likes " +
                    "FROM films AS f " +
                    "LEFT JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                    "GROUP BY f.film_id, f.name " +
                    "ORDER BY likes DESC " +
                    "LIMIT ?";
     private static final String GET_FILM_LIKES_QUERY = "SELECT * FROM film_likes WHERE film_id = ?";
     private static final String GET_ALL_QUERY = "SELECT * FROM films";
     private static final String GET_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";


    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film addFilm(Film film) {
        long id = insert(INSERT_INTO_QUERY,
                         film.getName(),
                         film.getDescription(),
                         film.getReleaseDate(),
                         film.getDuration(),
                         film.getMpaRatingId()
        );
        film.setId(id);
        return film;

    }

    @Override
    public Film updateFilm(Film film) {
        update(UPDATE_QUERY,
                 film.getName(),
                 film.getDescription(),
                 film.getDuration(),
                 film.getReleaseDate(),
                 film.getMpaRatingId()+1,
                 film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return findMany(GET_ALL_QUERY);
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return findOne(GET_BY_ID_QUERY,id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        insert(INSERT_INTO_LIKE_QUERY,filmId,userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
         delete(DELETE_LIKE_QUERY,userId,filmId);
    }

    @Override
    public List<Film> getTheMostPopularFilm(int count) {
        return findMany(GET_THE_MOST_POPULAR_QUERY,count);
    }




}
