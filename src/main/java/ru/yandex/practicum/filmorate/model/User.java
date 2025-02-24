package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Setter
@Getter
public class User {

    private Long id;
    @NotBlank(message = "имеил не может быть пустым")
    @Email(message = "неверный email формат")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @NotNull(message = "Поле с датой рождения не должна быть путсой")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;


}
