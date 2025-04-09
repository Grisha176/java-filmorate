package ru.yandex.practicum.filmorate.model;


import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.filmEnums.MpaRating;

@Setter
@Getter
public class FilmRating {
    private Integer id;
    private MpaRating rating;

    public FilmRating(){
    }
    public FilmRating(Integer id) {
        this.id = id;
    }


}
