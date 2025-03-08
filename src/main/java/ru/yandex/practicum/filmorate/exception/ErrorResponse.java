package ru.yandex.practicum.filmorate.exception;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorResponse {

    private final String error;

    public ErrorResponse(String error) {
        this.error = error;

    }
}
