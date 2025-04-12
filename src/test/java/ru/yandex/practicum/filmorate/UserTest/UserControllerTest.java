package ru.yandex.practicum.filmorate.UserTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
public class UserControllerTest {

    private final UserDbStorage userStorage;


    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userStorage.getUserById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
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



