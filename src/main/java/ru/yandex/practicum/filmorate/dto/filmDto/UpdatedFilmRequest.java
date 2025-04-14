package ru.yandex.practicum.filmorate.dto.filmDto;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class UpdatedFilmRequest {
    @NonNull
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Integer mpaRatingId;
    private Set<FilmGenre> filmGenre = new HashSet<>();


    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return !(releaseDate == null);
    }

    public boolean hasDuration() {
        return !(duration == 0);
    }

    public boolean hasRating() {
        return !(mpaRatingId == null);
    }

    public boolean hasGenre() {
        return !(filmGenre == null);
    }

}
