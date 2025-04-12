package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class BaseDbStorage<T> {
    private final JdbcTemplate jdbc;
    private final RowMapper<T> mapper;

    protected Optional<T> findOne(String query,Object... params){
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, Object... params){
          return jdbc.query(query,mapper,params);
    }

    protected Long insert(String query,Object... params){
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                if(params[idx] == null){
                    params[idx] = null;
                }
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey(); // Получаем ключ как Number
        if (key != null) {
            if (key instanceof Integer) {
                return ((Integer) key).longValue(); // Преобразуем Integer в Long
            } else if (key instanceof Long) {
                return (Long) key; // Возвращаем Long без преобразования
            } else {
                throw new RuntimeException("Неподдерживаемый тип сгенерированного ключа: " + key.getClass());
            }
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    protected void update(String query,Object... params){
      int rowsUpdated = jdbc.update(query,params);
      if (rowsUpdated == 0) {
          throw new InternalServerException("Не удалось обновить данные");
      }
    }
    protected boolean delete(String query,Object... params){
        int rowsUpdated = jdbc.update(query, params);
        return rowsUpdated > 0;
    }



}
