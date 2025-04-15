package ru.yandex.practicum.filmorate.dto.userDto;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {

    @NonNull
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public boolean hasUsername() {
        return !(name == null || name.isBlank());
    }

    public boolean hasEmail() {
        return !(email == null || email.isBlank());
    }

    public boolean hasLogin() {
        return !(login == null || login.isBlank());
    }

    public boolean hasBirthday() {
        return !(birthday == null || name.isBlank());
    }

}
