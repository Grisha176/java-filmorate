package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DeletedNotFoundFriendException;
import ru.yandex.practicum.filmorate.exception.InvalidFriendRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        validate(userId, friendId);
        if (userId.equals(friendId)) {
            log.trace("Попытка добавить в друзья самого себя - исключение");
            throw new InvalidFriendRequestException("Невозможно добавить в друзья самого себя");
        }
        userStorage.addFriend(userId,friendId);
        /*userStorage.getUserById(userId).getFriends().add(friendId);
        userStorage.getUserById(friendId).getFriends().add(userId);*/
        log.info("Успешное добавление в друзья");
    }

    public void deleteFriend(Long userId, Long friendId) {
        validate(userId, friendId);
        /*if (!userStorage.getUserById(userId).getFriends().contains(friendId)) {
            log.trace("Провал писка друга-выброс исключения");
            throw new DeletedNotFoundFriendException("Друг с id " + friendId + " не найден");
        }
        userStorage.getUserById(userId).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(userId);*/
        userStorage.deleteFriend(userId,friendId);
        log.info("Успешное удаление друга");
    }


    public Set<User> getMutualFriends(Long firstUserId, Long secondUserId) {
        validate(firstUserId, secondUserId);
       /* User firstUser = userStorage.getUserById(firstUserId);
        User secondUser = userStorage.getUserById(secondUserId);
        log.info("Поиск и возврат общих друзей");
        Set<User> mutualFriends = firstUser.getFriends().stream().filter(secondUser.getFriends()::contains).map(userStorage::getUserById).collect(Collectors.toSet());
        if (mutualFriends.isEmpty()) {
            log.trace("Общих друзей у пользователей с id: " + firstUserId + " и " + secondUser + " не найдено");
            throw new NotFoundException("Общих друзей у пользователей с id: " + firstUserId + " и " + secondUser + " не найдено");
        }*/
        Set<User> mutualFriends = userStorage.getMutualFriends(firstUserId,secondUserId);
        if (mutualFriends.isEmpty()) {
            log.trace("Общих друзей у пользователей с id: " + firstUserId + " и " + secondUserId + " не найдено");
            throw new NotFoundException("Общих друзей у пользователей с id: " + firstUserId + " и " + secondUserId + " не найдено");
        }
        return mutualFriends;
    }

    public Set<User> getUserFriend(Long userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            log.trace("Провал поиска user с id: " + userId);
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        log.info("Возврат поиcка друзей");
        return userStorage.getUserFriend(userId);
    }

    private void validate(Long firstId, Long secondId) {
        User user = userStorage.getUserById(firstId);
        User userAddedInFriendId = userStorage.getUserById(secondId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + firstId + " не найден");
        }
        if (userAddedInFriendId == null) {
            throw new NotFoundException("Пользователь с id " + secondId + " не найден");
        }
    }

}
