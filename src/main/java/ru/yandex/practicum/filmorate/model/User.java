package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data

public class User {

    private Long id;
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "неверный email формат")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @NotNull(message = "Поле с датой рождения не должна быть путсой")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

}

