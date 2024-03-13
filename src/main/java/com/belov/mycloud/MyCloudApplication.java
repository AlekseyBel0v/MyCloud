package com.belov.mycloud;

import com.belov.mycloud.domain.User;
import com.belov.mycloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@SpringBootApplication
public class MyCloudApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(MyCloudApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(User.builder().name("Alex").path("src/main/resources/static/alex").build());
    }
}