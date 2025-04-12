package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.filmEnums.Genre;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {

    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    private String description;
    @NotNull(message = "Дата релиза обязательна")
    @Past(message = "Дата релиза не может быть в будущем")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;
    private Set<FilmGenre> filmGenre = new HashSet<>();
    private FilmRating mpa;

    /*public String getMpa(){
        ObjectMapper mapper = new ObjectMapper();
        String filmRating;
        try {
            filmRating = mapper.writeValueAsString(mpa);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return filmRating;
    }
    /*public Integer getMpaId(){
        return mpa.getId();
    }*/

}