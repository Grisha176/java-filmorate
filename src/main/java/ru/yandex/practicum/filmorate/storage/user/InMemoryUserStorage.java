package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DeletedNotFoundFriendException;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.userEnums.friendshipStatus;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Map<Long, friendshipStatus>> userFriends = new HashMap<>();

    @Override
    public Collection<User> findAllUsers() {
        log.info("Получение всех пользователей");
        return users.values();
    }

    @Override
    public User addUser(User user) {
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

    @Override
    public User updateUser(User newUser) {
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

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        // Получаем карту друзей для обоих пользователей
        Map<Long, friendshipStatus> userFriendIds = userFriends.computeIfAbsent(userId, id -> new HashMap<>());
        Map<Long, friendshipStatus> fFriendIds = userFriends.computeIfAbsent(friendId, id -> new HashMap<>());
        // Проверяем статус дружбы между пользователями
        friendshipStatus statusFromFriend = fFriendIds.get(userId);
        if (friendshipStatus.CONFIRMED.equals(statusFromFriend)) {
            log.info("Пользователи {} и {} уже являются друзьями", userId, friendId);
            return;
        }
        if (friendshipStatus.PENDING.equals(statusFromFriend)) {
            // Если обратная заявка уже существует, подтверждаем дружбу
            userFriendIds.put(friendId, friendshipStatus.CONFIRMED);
            fFriendIds.put(userId, friendshipStatus.CONFIRMED);
            log.info("Пользователи {} и {} теперь друзья", userId, friendId);
        } else {
            // Если обратной заявки нет, создаем новую заявку со статусом PENDING
            userFriendIds.put(friendId, friendshipStatus.PENDING);
            log.info("Пользователь {} отправил заявку в друзья пользователю {}", userId, friendId);
        }
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        Map<Long, friendshipStatus> userFriendsIds = userFriends.get(userId);
        if (userFriendsIds == null) {
            throw new DeletedNotFoundFriendException("Друзья не найдены");
        }
        if (!userFriendsIds.containsKey(friendId)) {
            throw new DeletedNotFoundFriendException("Друг с id:" + friendId + " не найден");
        }
        userFriends.get(userId).remove(friendId);
        userFriends.get(friendId).remove(userId);

        log.info("Успешное удаление дружбы между пользователями {} и {}", userId, friendId);
    }

    @Override
    public Set<User> getMutualFriends(Long firstUserId, Long secondUserId) {
        Set<User> firstUserIdFriends = getConfirmedFriends(firstUserId);
        Set<User> secondUserIdFriends = getConfirmedFriends(secondUserId);
        return firstUserIdFriends.stream().filter(secondUserIdFriends::contains).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getUserFriend(Long userId) {
        Set<User> usersFriends = getConfirmedFriends(userId);
        if (usersFriends == null) {
            return new HashSet<>();
        }
        return usersFriends;
    }

    private long getNextId() {
        long id = users.keySet().stream().mapToLong(Long::longValue).max().orElse(0);
        return ++id;
    }

    private Set<User> getConfirmedFriends(Long userId) {
        return userFriends.getOrDefault(userId, Collections.emptyMap()).entrySet().stream()
                .filter(entry -> entry.getValue() == friendshipStatus.CONFIRMED)
                .map(Map.Entry::getKey)
                .map(users::get)
                .collect(Collectors.toSet());
    }
}
