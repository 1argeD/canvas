package com.painting.canvas;

import com.painting.canvas.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class CanvasApplication implements CommandLineRunner {

    @Autowired
   MemberRepository memberRepository;

    public static void main(String[] args) {
        SpringApplication.run(CanvasApplication.class, args);
    }

}
