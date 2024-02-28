package com.belov.mycloud.service;

import com.belov.mycloud.domain.User;
import com.belov.mycloud.domain.UserFile;
import com.belov.mycloud.repository.FileRepository;
import com.belov.mycloud.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.file.Paths.get;

@RequiredArgsConstructor
@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveFile(@NotNull String userName, @NotNull String fileName, @NotNull String hash, @NotNull MultipartFile file) throws IOException {
        //проверим, существует ли файл с именем fileName
        Optional<UserFile> fileFromRepo = fileRepository.findByNameAndDeletedFalseAndUserName(fileName, userName);
        if (fileFromRepo.isEmpty()) {
            User user = userRepository.findByName(userName);
            //создаю файл для записи на диск
            Path fileForWrite = get(user.getPath(), fileName);
            try {
                file.transferTo(fileForWrite);  //запись файла
            } catch (IOException e) {
                throw new IOException(e);
            }
            //сохраняю информацию в базе
            fileRepository.save(UserFile.builder().user(user).fileHash(hash).name(fileName).build());
        }
    }
}