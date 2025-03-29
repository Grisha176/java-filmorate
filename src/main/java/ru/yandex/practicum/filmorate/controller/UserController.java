package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;


@RestController
@Slf4j
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;


    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    public UserController() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
    }


    @GetMapping("/users")
    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUsersById(@PathVariable Long id) {
        return userStorage.getUserById(id);
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody final User user) {
        return userStorage.addUser(user);
    }


    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User newUser) {
        return userStorage.updateUser(newUser);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        userService.addFriend(userId, friendId);
        return new ResponseEntity<>("{ \"message\": \"Добавление прошло успешно!\" }", HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteFromFriend(@PathVariable("id") Long userId, @PathVariable Long friendId) {
        userService.deleteFriend(userId, friendId);
        return new ResponseEntity<>("{ \"message\": \"Удаление прошло успешено!\" }", HttpStatus.OK);
    }

    @GetMapping("/users/{id}/friends")
    public Set<User> getUserFriend(@PathVariable Long id) {
        return userService.getUserFriend(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<User> getMutualFriends(@PathVariable("id") Long firstUserId, @PathVariable("otherId") Long secondUserId) {
        return userService.getMutualFriends(firstUserId, secondUserId);
    }


}
