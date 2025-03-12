package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

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

    private long getNextId() {
        long id = users.keySet().stream().mapToLong(Long::longValue).max().orElse(0);
        return ++id;
    }
}
