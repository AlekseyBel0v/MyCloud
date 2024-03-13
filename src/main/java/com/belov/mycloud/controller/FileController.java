package com.belov.mycloud.controller;

import com.belov.mycloud.repository.UserRepository;
import com.belov.mycloud.service.FileService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.annotation.Secured;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String saveFileOnServer(
            // todo: заменить имя юзера в кверистринг на авторизацию по токену
            @RequestParam String userName,
            @NotNull(message = "Имя файла не указано. Необходимо указать.")
            @RequestParam String fileName,

            @NotNull(message = "Хэш файла не указан. Необходимо указать.")
            @RequestParam String hash,

            @NotNull(message = "Файл отсутствует. Необходимо добавить файл.")
            @RequestPart MultipartFile file) throws Exception {
        // todo: заменить id = 999 на автоматическую генерацию
        fileService.saveFile(999, userName, fileName, hash, file);
        return "Success upload";
    }

    @DeleteMapping
    public String deleteFile(
            // todo: заменить имя юзера в кверистринг на авторизацию по токену
            @RequestParam String userName,

            @NotNull(message = "Имя файла не указано. Необходимо указать.")
            @RequestParam String fileName) throws Exception {
        // todo: заменить id = 999 на автоматическую генерацию
        fileService.deleteFile(999, userName, fileName);
        return "Success deleted";
    }

    @GetMapping
    public ResponseEntity<MultiValueMap<String, String>> getFile(
            // todo: заменить имя юзера в кверистринг на авторизацию по токену
            @RequestParam String userName,
            @NotNull(message = "Имя файла не указано. Необходимо указать.")
            @RequestParam String fileName) throws Exception {
        // todo: заменить id = 999 на автоматическую генерацию
        MultiValueMap<String, String> body = fileService.getFile(999, userName, fileName);
        return ResponseEntity.
                ok().
                header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE).
                body(body);
    }
}