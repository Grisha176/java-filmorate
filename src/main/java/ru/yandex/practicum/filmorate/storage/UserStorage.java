package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.userEnums.FriendShipStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface UserStorage {

    Collection<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(Long id);

    void addFriend(Long userId, Long friendId, FriendShipStatus status);

    boolean deleteFriend(Long userId,Long friendId);

    List<User> getCommonFriends(Long firstUserId,Long secondUserId);

    List<User> getUserFriend(Long userId);

}
