package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.practicum.client.StatisticClient;

@SpringBootApplication
public class EVMServerApp {
    public static void main(String[] args) {
        SpringApplication.run(EVMServerApp.class, args);
    }
}