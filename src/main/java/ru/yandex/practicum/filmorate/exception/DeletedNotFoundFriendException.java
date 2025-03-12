package ru.yandex.practicum.filmorate.exception;

public class DeletedNotFoundFriendException extends RuntimeException {
    public DeletedNotFoundFriendException(String message) {
        super(message);
    }
}
