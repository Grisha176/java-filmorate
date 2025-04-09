package ru.yandex.practicum.filmorate.storage.dao;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.util.List;
import java.util.Optional;

@Repository
public class RatingDbStorage extends BaseDbStorage<FilmRating>{

    public RatingDbStorage(JdbcTemplate jdbc, RowMapper<FilmRating> mapper) {
        super(jdbc, mapper);
    }

    private static final String GET_ALL_RATING_QUERY = "SELECT * FROM mpa_rating";
    private static final String GET_RATING_BY_ID_QUERY = "SELECT * FROM mpa_rating WHERE mpa_rating_id = ?";

    public List<FilmRating> getAllRating(){
        return findMany(GET_ALL_RATING_QUERY);
    }

    public Optional<FilmRating> getFilmRating(int id){
        return findOne(GET_RATING_BY_ID_QUERY, id);
    }




}
