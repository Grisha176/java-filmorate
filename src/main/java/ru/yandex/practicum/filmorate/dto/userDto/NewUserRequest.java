package ru.yandex.practicum.filmorate.dto.userDto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NewUserRequest {
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "неверный email формат")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @NotNull(message = "Поле с датой рождения не должна быть пустой")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;

}
