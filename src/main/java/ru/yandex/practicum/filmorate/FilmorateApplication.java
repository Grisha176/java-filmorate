package ru.yandex.practicum.filmorate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = "ru.yandex.practicum.filmorate")
public class FilmorateApplication {

    @Value("${app.custom-property}")
    private String customProperty;

    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }

    @PostConstruct
    public void printCustomProperty() {
        System.out.println(customProperty);
    }
}