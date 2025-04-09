package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.userEnums.FriendShipStatus;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Repository("UserDbStorage")
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {

    private final static String FIND_ALL_QUERY = "SELECT * FROM users";
    private final static String INSERT_INTO_QUERY =
            "INSERT INTO users(name, email, login, birthday) " +
                    "VALUES(?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE users SET name = ?,email = ?, login = ?,birthday = ? WHERE user_id = ?";
    private static final String FIND_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO user_friends(user_id,friend_id,status) VALUES(?,?,?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";
    private static final String GET_COMMON_FRIENDS =
            "SELECT u.* " +
                    "FROM users u " +
                    "WHERE u.user_id IN ( " +
                    "    SELECT f1.friend_id " +
                    "    FROM user_friends f1 " +
                    "    JOIN user_friends f2 ON f1.friend_id = f2.friend_id " +
                    "    WHERE f1.user_id = ? AND f2.user_id = ? AND f1.status = 'confirmed'" +
                    ")";
    private static final String GET_USER_FRIEND_QUERY = "SELECT u.* \n" +
            "FROM users u\n" +
            "JOIN user_friends uf ON u.user_id = uf.friend_id\n" +
            "WHERE uf.user_id = ? AND uf.status = 'confirmed';";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> getAllUsers() {
        return findMany(FIND_ALL_QUERY);
    }
    @Override
    public Optional<User> getUserById(Long id) {
        return findOne(FIND_QUERY,id);
    }

    @Override
    public User addUser(User user) {
        long id = insert(INSERT_INTO_QUERY,
                           user.getName(),
                           user.getEmail(),
                           user.getLogin(),
                           user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(UPDATE_QUERY,
                    user.getName(),
                    user.getEmail(),
                    user.getLogin(),
                    user.getBirthday(),
                    user.getId());
        return user;
    }



    @Override
    public void addFriend(Long userId, Long friendId, FriendShipStatus status) {
        insert(INSERT_FRIEND_QUERY,userId,friendId,String.valueOf(status));
    }

    @Override
    public boolean deleteFriend(Long userId, Long friendId) {
          return delete(DELETE_FRIEND_QUERY,userId,friendId);
    }

    @Override
    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
         return findMany(GET_COMMON_FRIENDS,firstUserId,secondUserId);
    }

    @Override
    public List<User> getUserFriend(Long userId) {
        return findMany(GET_USER_FRIEND_QUERY,userId);
    }
}
