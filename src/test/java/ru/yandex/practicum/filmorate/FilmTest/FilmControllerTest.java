package ru.yandex.practicum.filmorate.FilmTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FilmControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(FilmController.class).build();
    }

    @Test
    void addFilmReturnStatus200() throws Exception {
        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("This is a valid description.");
        film.setReleaseDate(LocalDate.of(1999,1,10));
        film.setDuration(120);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String filmJson = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().is(200));
    }

    @Test
    void createFilmEmptyNameReturnBadRequest() throws Exception {

        Film film = new Film();
        film.setId(1L);
        film.setName(""); // Пустое имя
        film.setDescription("description.");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilmFutureReleaseReturnBadRequest() throws Exception {

        Film film = new Film();
        film.setId(1L);
        film.setName("name"); // Пустое имя
        film.setDescription("description.");
        film.setReleaseDate(LocalDate.now().plusDays(1));
        film.setDuration(120);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilmInvalidDurationReturnBadRequest() throws Exception {

        Film film = new Film();
        film.setId(1L);
        film.setName("name"); // Пустое имя
        film.setDescription("description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-120);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFilmReturnStatus200() throws Exception {
        Film film = new Film();
        film.setId(1L);
        film.setName("name");
        film.setDescription("description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String filmJson = objectMapper.writeValueAsString(film);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson))
                .andExpect(status().is(200));


        Film updateFilm = new Film();
        updateFilm.setId(1L);
        updateFilm.setName("updateName");
        updateFilm.setDescription("updateDescription");
        updateFilm.setReleaseDate(LocalDate.of(2020, 1, 1));
        updateFilm.setDuration(40);

        String updateFilmJson = objectMapper.writeValueAsString(updateFilm);

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateFilmJson))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name").value("updateName"))
                .andExpect(jsonPath("$.description").value("updateDescription"))
                .andExpect(jsonPath("$.duration").value(40));

    }


}
