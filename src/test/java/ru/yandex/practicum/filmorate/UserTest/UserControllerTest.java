package ru.yandex.practicum.filmorate.UserTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.model.userEnums.FriendShipStatus;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class})
public class UserControllerTest {

    private final UserDbStorage userStorage;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        userStorage.deleteAllUsers();
        user1 = new User();
        user1.setEmail("example@mail.ru");
        user1.setLogin("testLogin");
        user1.setName("name");
        user1.setBirthday(LocalDate.of(2000, 11, 10));
        user1 = userStorage.addUser(user1);

        user2 = new User();
        user2.setEmail("mail@mail.ru");
        user2.setLogin("Login");
        user2.setName("name");
        user2.setBirthday(LocalDate.of(2020, 11, 10));
        userStorage.addUser(user2);
    }

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userStorage.getUserById(user1.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user.getId()).isEqualTo(user1.getId());
                    assertThat(user.getEmail()).isEqualTo(user1.getEmail());
                    assertThat(user.getLogin()).isEqualTo(user1.getLogin());
                    assertThat(user.getName()).isEqualTo(user1.getName());
                    assertThat(user.getBirthday()).isEqualTo(user1.getBirthday());
                });
    }

    @Test
    public void testFindAllUsers() {

        List<User> users = userStorage.getAllUsers();

        assertThat(users).isNotEmpty();
        assertThat(users)
                .hasSize(2)
                .anySatisfy(user -> {
                    assertThat(user.getId()).isEqualTo(user1.getId());
                    assertThat(user.getEmail()).isEqualTo(user1.getEmail());
                    assertThat(user.getLogin()).isEqualTo(user1.getLogin());
                    assertThat(user.getName()).isEqualTo(user1.getName());
                    assertThat(user.getBirthday()).isEqualTo(user1.getBirthday());
                })
                .anySatisfy(user -> {
                    assertThat(user.getId()).isEqualTo(2);
                    assertThat(user.getEmail()).isEqualTo(user2.getEmail());
                    assertThat(user.getLogin()).isEqualTo(user2.getLogin());
                    assertThat(user.getName()).isEqualTo(user2.getName());
                    assertThat(user.getBirthday()).isEqualTo(user2.getBirthday());
                });
    }

    @Test
    public void testUpdateUser() {
        user1.setEmail("newEmail@mail.ru");
        user1.setName("new_name");
        user1.setBirthday(LocalDate.of(1995, 12, 12));
        userStorage.updateUser(user1);

        Optional<User> optionalUser = userStorage.getUserById(user1.getId());

        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user.getId()).isEqualTo(user1.getId());
                    assertThat(user.getEmail()).isEqualTo(user1.getEmail());
                    assertThat(user.getLogin()).isEqualTo(user1.getLogin());
                    assertThat(user.getName()).isEqualTo(user1.getName());
                    assertThat(user.getBirthday()).isEqualTo(user1.getBirthday());
                });

    }

    @Test
    public void deleteFriend() {
        userStorage.addFriend(user1.getId(), user2.getId(), FriendShipStatus.CONFIRMED);

        List<User> userFriend = userStorage.getUserFriend(user1.getId());

        assertThat(userFriend)
                .hasSize(1)
                .anySatisfy(user -> {
                    assertThat(user.getId()).isEqualTo(user2.getId());
                    assertThat(user.getEmail()).isEqualTo(user2.getEmail());
                    assertThat(user.getLogin()).isEqualTo(user2.getLogin());
                    assertThat(user.getName()).isEqualTo(user2.getName());
                    assertThat(user.getBirthday()).isEqualTo(user2.getBirthday());
                });

        userStorage.deleteFriend(user1.getId(), user2.getId());
        userFriend = userStorage.getUserFriend(user1.getId());

        assertThat(userFriend).isEmpty();
    }

    @Test
    public void getCommonFriend() {
        User user3 = new User();
        user3.setEmail("mail2@mail.ru");
        user3.setLogin("login2");
        user3.setName("name2");
        user3.setBirthday(LocalDate.of(1980, 1, 12));
        userStorage.addUser(user3);

        userStorage.addFriend(user1.getId(), user2.getId(), FriendShipStatus.CONFIRMED);
        userStorage.addFriend(user3.getId(), user2.getId(), FriendShipStatus.CONFIRMED);

        List<User> commonFriend = userStorage.getCommonFriends(user1.getId(), user3.getId());

        assertThat(commonFriend)
                .isNotEmpty()
                .anySatisfy(user -> {
                    assertThat(user.getId()).isEqualTo(user2.getId());
                    assertThat(user.getEmail()).isEqualTo(user2.getEmail());
                    assertThat(user.getLogin()).isEqualTo(user2.getLogin());
                    assertThat(user.getName()).isEqualTo(user2.getName());
                    assertThat(user.getBirthday()).isEqualTo(user2.getBirthday());
                });
    }


}





















        /*private MockMvc mockMvc;
        private final UserStorage userStorage;

        @Autowired
        public UserControllerTest(UserStorage userStorage) {
            this.userStorage = userStorage;
        }

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);
            mockMvc = MockMvcBuilders.standaloneSetup(UserController.class).build();
        }

        @Test
        public void createUserValidUserIs200() throws Exception {
            User user = new User();
            user.setId(1L);
            user.setName("name");
            user.setLogin("login");
            user.setEmail("yandex@mail.ru");
            user.setBirthday(LocalDate.of(2000, 10, 10));

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String userJson = objectMapper.writeValueAsString(user);

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userJson))
                    .andExpect(status().is(200));

        }

        @Test
        void createUserInvalidEmailReturnBadRequest() throws Exception {
            User user = new User();
            user.setId(1L);
            user.setName("name");
            user.setLogin("login");
            user.setEmail("badEmail");
            user.setBirthday(LocalDate.of(2000, 10, 10));

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String userJson = objectMapper.writeValueAsString(user);

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userJson))
                    .andExpect(status().isBadRequest());


        }

        @Test
        void createUserEmptyLoginReturnBadRequest() throws Exception {
            User user = new User();
            user.setId(1L);
            user.setName("name");
            user.setLogin("");
            user.setEmail("yandex@mail.ru");
            user.setBirthday(LocalDate.of(2000, 10, 10));

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String userJson = objectMapper.writeValueAsString(user);

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userJson))
                    .andExpect(status().isBadRequest());

        }

        @Test
        void updateUserReturnStatus200() throws Exception {
            User newUser = new User();
            newUser.setId(1L);
            newUser.setEmail("yandex@example.com");
            newUser.setLogin("login");
            newUser.setName("nameUser");
            newUser.setBirthday(LocalDate.of(2000, 1, 1));
            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(newUser)))
                    .andExpect(status().is(200));
            User updatedUser = new User();
            updatedUser.setId(1L); // Убедитесь, что ID установлен
            updatedUser.setEmail("updated@example.com");
            updatedUser.setLogin("updatedUser");
            updatedUser.setName("updateName");
            updatedUser.setBirthday(LocalDate.of(1990, 1, 1));
            mockMvc.perform(put("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(updatedUser)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("updated@example.com"))
                    .andExpect(jsonPath("$.login").value("updatedUser"))
                    .andExpect(jsonPath("$.name").value("updateName"));
        }


        @Test
        void getUsersReturnAllUsers() throws Exception {

            User user = new User();
            user.setId(1L);
            user.setName("name");
            user.setLogin("login");
            user.setEmail("user@example.ru");
            user.setBirthday(LocalDate.of(2000, 10, 10));

            User user2 = new User();
            user.setId(2L);
            user.setName("name2");
            user.setLogin("login2");
            user.setEmail("user2@example.ru");
            user.setBirthday(LocalDate.of(2000, 10, 10));
            userStorage.addUser(user);
            userStorage.addUser(user2);

            mockMvc.perform(get("/users")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());


        }*/



