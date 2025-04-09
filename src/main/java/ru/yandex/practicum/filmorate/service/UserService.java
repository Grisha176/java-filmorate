package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.userDto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.userDto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.userDto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.userEnums.FriendShipStatus;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserService {
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto getUserById(Long id){
        return UserMapper.mapToUserDto(userStorage.getUserById(id).orElseThrow(()-> new NotFoundException("Пользователь с id:"+id+" не найден")));
    }

    public List<UserDto> getAllUsers(){
        return userStorage.getAllUsers().stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    public UserDto addUser(NewUserRequest userRequest){
        User user = UserMapper.mapToUser(userRequest);
        user = userStorage.addUser(user);
        return UserMapper.mapToUserDto(user);
    }
    public UserDto updateUser(Long userId, UpdateUserRequest updateUserRequest){
        User updatedUser = userStorage.getUserById(userId)
                .map(user -> UserMapper.updateUserFields(user, updateUserRequest))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        updatedUser = userStorage.updateUser(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }


    public void addFriend(Long userId, Long friendId) {
        validate(userId,friendId);
        userStorage.addFriend(userId,friendId, FriendShipStatus.CONFIRMED);

    }

    public boolean deleteFriend(Long userId, Long friendId) {
        validate(userId,friendId);
         return userStorage.deleteFriend(userId,friendId);
    }


    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
         validate(firstUserId,secondUserId);
         return userStorage.getCommonFriends(firstUserId,secondUserId);
    }

    public List<User> getUserFriend(Long userId) {
          validate(userId,userId);
         return userStorage.getUserFriend(userId);
    }


    private void validate(Long firstId, Long secondId) {
        User user = userStorage.getUserById(firstId).orElseThrow(() -> new NotFoundException("Пользователь с id " + firstId + " не найден"));
        User userAddedInFriendId = userStorage.getUserById(secondId).orElseThrow(() -> new NotFoundException("Пользователь с id " + firstId + " не найден"));
    }



}
