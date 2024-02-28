package com.belov.mycloud.controller;

import com.belov.mycloud.exception.EmptyFileException;
import com.belov.mycloud.repository.FileRepository;
import com.belov.mycloud.repository.UserRepository;
import com.belov.mycloud.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;

@RestController
@RequestMapping("/file")
public class FileController {

    private FileService fileService = new FileService();

    @Autowired
    private UserRepository userRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // todo: заменить потом имя юзера в кверистринг на авторизацию по токену
    public String saveFileOnServer(@RequestParam String userName, @RequestParam String fileName, @RequestParam String hash, @RequestPart MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            fileService.saveFile(userName, fileName, hash, file);
        } else throw new EmptyFileException("Choose the file");
        return "Success upload";
    }
}
