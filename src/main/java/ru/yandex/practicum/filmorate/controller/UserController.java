package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.Valid;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("Получение всех пользователей");
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody final User user) {

        boolean hasUser = users.values().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()));
        if (hasUser) {
            log.error("Email {} уже используется", user.getEmail());
            throw new DuplicateException("Email:" + user.getEmail() + " уже используется");
        }

        user.setId(getNextId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Добавление пользователя прошло успешно");
        return user;
    }


    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        User oldUser = users.get(newUser.getId());

        if (oldUser == null) {
            log.info("Пользователь с id " + newUser.getId() + " не найден");
            throw new NotFoundException("User с id = " + newUser.getId() + " не найден");
        }

        boolean hasUserWithEmail = users.values().stream().filter(user -> user.getEmail() != oldUser.getEmail()).anyMatch(user -> user.getEmail() == newUser.getEmail());
        if (hasUserWithEmail) {
            log.warn("Email {} уже используется", newUser.getEmail());
            throw new DuplicateException("Email:" + newUser.getEmail() + " уже используется");
        }
        if (newUser.getId() == null) {
            log.trace("Айди - обязательное поле");
            throw new ValidationException("Id это обязательное поле");
        }

        oldUser.setName(newUser.getName());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setBirthday(newUser.getBirthday());
        log.info("Обновление пользователя прошло успешно");
        return oldUser;
    }

    private long getNextId() {
        long id = users.keySet().stream().mapToLong(Long::longValue).max().orElse(0);
        return ++id;
    }


}
