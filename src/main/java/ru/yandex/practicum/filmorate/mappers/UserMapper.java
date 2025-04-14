package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.userDto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.userDto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.userDto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {

    public static User mapToUser(NewUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setName(request.getName());
        user.setBirthday(request.getBirthday());
        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setName(user.getName());
        userDto.setBirthday(user.getBirthday());
        return userDto;
    }

    public static User updateUserFields(User user, UpdateUserRequest updateUserRequest) {
        if (updateUserRequest.hasBirthday()) {
            user.setBirthday(updateUserRequest.getBirthday());
        }
        if (updateUserRequest.hasEmail()) {
            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.hasUsername()) {
            user.setName(updateUserRequest.getName());
        }
        if (updateUserRequest.hasLogin()) {
            user.setLogin(updateUserRequest.getLogin());
        }
        return user;
    }
}
