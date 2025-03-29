package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;


public interface UserStorage {

    Collection<User> findAllUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    void addFriend(Long userId,Long friendId);

    void deleteFriend(Long userId,Long friendId);

    Set<User> getMutualFriends(Long firstUserId,Long secondUserId);

    Set<User> getUserFriend(Long userId);

}
