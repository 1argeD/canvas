package com.painting.canvas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories
public class CanvasApplication{

    public static void main(String[] args) {
        SpringApplication.run(CanvasApplication.class, args);
    }

}
