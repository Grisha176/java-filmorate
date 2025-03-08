package ru.yandex.practicum.filmorate.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse("Объект не найден");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleDuplicateException(DuplicateException e) {
        return new ErrorResponse("Дублирование объекта!!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateException(ValidationException e) {
        return new ErrorResponse("Ошибка валдации,проверьте корректность данных");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidFriendRequestException(InvalidFriendRequestException e) {
        return new ErrorResponse("Ошибка при добалении в друзья,проверьте правильность параметров");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse handleNotFoundFriend(DeletedNotFoundFriendException e) {
        return new ErrorResponse("Этот пользователь не является вашим другом");
    }


}
