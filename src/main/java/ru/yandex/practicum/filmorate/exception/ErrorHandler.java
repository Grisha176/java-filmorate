package ru.yandex.practicum.filmorate.exception;


import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        log.warn("Обьект не бвл найден");
        return new ErrorResponse("Объект не найден");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleDuplicateException(DuplicateException e) {
        log.warn("Повторяющийся обьект-привело к ошибке");
        return new ErrorResponse("Дублирование объекта!!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateException(ValidationException e) {
        log.warn("Ошибка при валидации");
        return new ErrorResponse("Ошибка валидации,проверьте корректность данных");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidFriendRequestException(InvalidFriendRequestException e) {
        log.warn("Неправильные параметры при запрос в друзья");
        return new ErrorResponse("Ошибка при добавлении в друзья,проверьте правильность параметров");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse handleNotFoundFriend(DeletedNotFoundFriendException e) {
        log.warn("Неверное указание друга,указан пользователь, не являющийся другом");
        return new ErrorResponse("Этот пользователь не является вашим другом");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidValidate(MethodArgumentNotValidException e) {
        log.warn("Ошибка валидации данных," + e.getMessage());
        return new ErrorResponse("Ошибка валидации данных");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("Ошибка валидации данных," + e.getMessage());
        return new ErrorResponse("Ошибка валидации данных");
    }


}
