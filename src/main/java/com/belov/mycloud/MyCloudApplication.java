package com.belov.mycloud;

import com.belov.mycloud.model.entity.User;
import com.belov.mycloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashSet;

@RequiredArgsConstructor
@SpringBootApplication
public class MyCloudApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Value("${file_root_for_macOS}")
    private String fileRoot;

    public static void main(String[] args) {
        SpringApplication.run(MyCloudApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initDirectory(fileRoot);
        userRepository.save(User.builder().name("Alex").path(fileRoot + "/Alex").build());
    }

    private void initDirectory(String root) throws IOException {
        Path rootDir = Paths.get(root);
        if (Files.notExists(rootDir)) {
            Files.createDirectories(rootDir);
        }
    }
}