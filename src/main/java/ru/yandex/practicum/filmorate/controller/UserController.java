package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.userDto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.userDto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.userDto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;


@RestController
@Slf4j
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserDto getUsersById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public UserDto addUser(@Valid @RequestBody final NewUserRequest user) {
        return userService.addUser(user);
    }


    @PutMapping("/users")
    public UserDto updateUser(@Valid @RequestBody UpdateUserRequest user) {
        return userService.updateUser(user.getId(), user);
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
    public List<User> getUserFriend(@PathVariable Long id) {
        return userService.getUserFriend(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") Long firstUserId, @PathVariable("otherId") Long secondUserId) {
        return userService.getCommonFriends(firstUserId, secondUserId);
    }


}
