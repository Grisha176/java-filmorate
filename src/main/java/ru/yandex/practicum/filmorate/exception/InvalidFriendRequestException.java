package ru.yandex.practicum.filmorate.exception;

public class InvalidFriendRequestException extends RuntimeException {
    public InvalidFriendRequestException(String message) {
        super(message);
    }
}
