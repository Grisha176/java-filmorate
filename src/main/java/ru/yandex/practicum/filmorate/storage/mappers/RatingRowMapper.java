package ru.yandex.practicum.filmorate.storage.mappers;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.model.filmEnums.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingRowMapper implements RowMapper<FilmRating> {

    @Override
    public FilmRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmRating filmRating = new FilmRating();
        filmRating.setId(rs.getInt("mpa_rating_id"));
        filmRating.setRating(MpaRating.valueOf(rs.getString("rating")));
        return filmRating;
    }
}
