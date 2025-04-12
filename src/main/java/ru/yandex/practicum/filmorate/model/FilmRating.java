package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.filmEnums.MpaRating;

@Data
@AllArgsConstructor
public class FilmRating {
    private Integer id;
    private String name;
    public FilmRating(){

    }

}
