package ru.yandex.practicum.filmorate.dto.filmDto;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class UpdatedFilmRequest {
    @NonNull
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Integer mpaRatingId;

    public boolean hasName() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return ! (description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return ! (releaseDate == null);
    }
    public boolean hasDuration(){return ! (duration == 0);}

    public boolean hasRating(){return ! (mpaRatingId == null);}
}
