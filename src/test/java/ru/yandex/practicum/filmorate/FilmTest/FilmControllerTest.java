package ru.yandex.practicum.filmorate.FilmTest;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmRowMapper.class, RatingDbStorage.class, RatingRowMapper.class, GenreDbStorage.class, GenreRowMapper.class, UserDbStorage.class, UserRowMapper.class})
public class FilmControllerTest {

    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final RatingDbStorage mpaStorage;
    private final GenreDbStorage genreStorage;

    private Film film1;
    private Film film2;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        film1 = new Film();
        film1.setName("name"); // Пустое имя
        film1.setDescription("description.");
        film1.setReleaseDate(LocalDate.now());
        film1.setDuration(120);
        film1.setMpa(new FilmRating(1, "G"));
        Set<FilmGenre> filmGenre = new HashSet<>();
        filmGenre.add(new FilmGenre(1L, "Комедия"));
        film1.setFilmGenre(filmGenre);
        film1 = filmStorage.addFilm(film1);

        film2 = new Film();
        film2.setName("film"); // Пустое имя
        film2.setDescription("description2");
        film2.setReleaseDate(LocalDate.now());
        film2.setDuration(60);
        film2.setMpa(new FilmRating(4, "NC-17"));
        film2 = filmStorage.addFilm(film2);

        user1 = new User();
        user1.setEmail("example@mail.ru");
        user1.setLogin("testLogin");
        user1.setName("name");
        user1.setBirthday(LocalDate.of(2000, 11, 10));
        user1 = userStorage.addUser(user1);

        user2 = new User();
        user2.setEmail("mail@mail.ru");
        user2.setLogin("login2");
        user2.setName("user");
        user2.setBirthday(LocalDate.of(1995, 2, 25));
        user2 = userStorage.addUser(user2);
    }

    @Test
    public void testFindFilmById() {
        Optional<Film> filmOptional = filmStorage.getFilmById(film1.getId());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film.getId()).isEqualTo(film1.getId());
                    assertThat(film.getName()).isEqualTo(film1.getName());
                    assertThat(film.getDescription()).isEqualTo(film1.getDescription());
                    assertThat(film.getDuration()).isEqualTo(film1.getDuration());
                    assertThat(film.getMpa()).isEqualTo(film1.getMpa());
                    assertThat(film.getReleaseDate()).isEqualTo(film1.getReleaseDate());
                });
    }

    @Test
    public void testFindAllFilms() {

        List<Film> films = filmStorage.getAllFilms();

        assertThat(films)
                .hasSize(2)
                .anySatisfy(film -> {
                    assertThat(film.getId()).isEqualTo(film1.getId());
                    assertThat(film.getName()).isEqualTo(film1.getName());
                    assertThat(film.getDescription()).isEqualTo(film1.getDescription());
                    assertThat(film.getDuration()).isEqualTo(film1.getDuration());
                    assertThat(film.getMpa()).isEqualTo(film1.getMpa());
                    assertThat(film.getReleaseDate()).isEqualTo(film1.getReleaseDate());
                })
                .anySatisfy(film -> {
                    assertThat(film.getId()).isEqualTo(film2.getId());
                    assertThat(film.getName()).isEqualTo(film2.getName());
                    assertThat(film.getDescription()).isEqualTo(film2.getDescription());
                    assertThat(film.getDuration()).isEqualTo(film2.getDuration());
                    assertThat(film.getMpa()).isEqualTo(film2.getMpa());
                    assertThat(film.getReleaseDate()).isEqualTo(film2.getReleaseDate());
                });
    }

    @Test
    public void testUpdateFilm() {
        film1.setName("updatedName");
        film1.setDescription("updatedDescription");
        film1.setDuration(70);
        film1.setMpa(new FilmRating(5, "NC-17"));
        filmStorage.updateFilm(film1);

        Optional<Film> filmOptional = filmStorage.getFilmById(film1.getId());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film.getId()).isEqualTo(film1.getId());
                    assertThat(film.getName()).isEqualTo(film1.getName());
                    assertThat(film.getDescription()).isEqualTo(film1.getDescription());
                    assertThat(film.getDuration()).isEqualTo(film1.getDuration());
                    assertThat(film.getMpa()).isEqualTo(film1.getMpa());
                    assertThat(film.getReleaseDate()).isEqualTo(film1.getReleaseDate());
                });
    }

    @Test
    public void testGetTheMostPopularFilmLike() {
        filmStorage.addLike(film1.getId(), user1.getId());
        filmStorage.addLike(film1.getId(), user2.getId());
        filmStorage.addLike(film2.getId(), user1.getId());

        List<Film> popularFilms = filmStorage.getTheMostPopularFilm(10);

        assertThat(popularFilms)
                .hasSize(2);

        assertThat(popularFilms)
                .element(0)
                .extracting(Film::getId)
                .isEqualTo(film1.getId());

        assertThat(popularFilms)
                .element(1)
                .extracting(Film::getId)
                .isEqualTo(film2.getId());
    }

    @Test
    public void getMpaById() {
        Optional<FilmRating> mpa = mpaStorage.getFilmRating(1);

        assertThat(mpa)
                .isPresent()
                .hasValueSatisfying(rating -> {
                    assertThat(rating.getName()).isEqualTo("G");
                });
    }

    @Test
    public void getAllMpa() {

        List<FilmRating> mpa = mpaStorage.getAllRating();

        assertThat(mpa)
                .anySatisfy(filmRating -> {
                    assertThat(filmRating.getName()).isEqualTo("G");
                })
                .anySatisfy(filmRating -> {
                    assertThat(filmRating.getName()).isEqualTo("PG");
                })
                .anySatisfy(filmRating -> {
                    assertThat(filmRating.getName()).isEqualTo("PG-13");
                })
                .anySatisfy(filmRating -> {
                    assertThat(filmRating.getName()).isEqualTo("R");
                })
                .anySatisfy(filmRating -> {
                    assertThat(filmRating.getName()).isEqualTo("NC-17");
                });
    }

    @Test
    public void getGenreById() {
        Optional<FilmGenre> genre = genreStorage.getFilmGenreById(1);

        assertThat(genre)
                .isPresent()
                .hasValueSatisfying(rating -> {
                    assertThat(rating.getName()).isEqualTo("Комедия");
                });
    }

    @Test
    public void getAllGenre() {

        List<FilmGenre> genres = genreStorage.getAllGenre();

        assertThat(genres)
                .anySatisfy(genre -> {
                    assertThat(genre.getName()).isEqualTo("Комедия");
                })
                .anySatisfy(genre -> {
                    assertThat(genre.getName()).isEqualTo("Драма");
                })
                .anySatisfy(genre -> {
                    assertThat(genre.getName()).isEqualTo("Мультфильм");
                })
                .anySatisfy(genre -> {
                    assertThat(genre.getName()).isEqualTo("Триллер");
                })
                .anySatisfy(genre -> {
                    assertThat(genre.getName()).isEqualTo("Документальный");
                })
                .anySatisfy(genre -> {
                    assertThat(genre.getName()).isEqualTo("Боевик");
                });
    }

    @Test
    public void getFilmGenres() {
        List<FilmGenre> genres = genreStorage.getFilmGenre(film1.getId());

        assertThat(genres)
                .hasSize(1)
                .anySatisfy(genre -> {
                    assertThat(genre.getName()).isEqualTo("Комедия");
                });
    }


}
