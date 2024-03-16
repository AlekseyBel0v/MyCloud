package com.belov.mycloud.controller;

import com.belov.mycloud.exception.CustomException;
import com.belov.mycloud.model.front.FileDescription;
import com.belov.mycloud.model.front.JsonName;
import com.belov.mycloud.repository.UserRepository;
import com.belov.mycloud.service.FileService;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FileController {

    private final FileService fileService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String saveFileOnServer(
            // todo: заменить имя юзера в кверистринг на авторизацию по токену
            @RequestParam String userName,
            @NotNull(message = "Имя файла не указано. Необходимо указать.")
            @RequestParam String filename,

            @NotNull(message = "Хэш файла не указан. Необходимо указать.")
            @RequestParam String hash,

            @NotNull(message = "Файл отсутствует. Необходимо добавить файл.")
            @RequestPart MultipartFile file) throws Exception {
        // todo: заменить id = 999 на автоматическую генерацию
        fileService.saveFile(999, userName, filename, hash, file);
        return "Success upload";
    }

    @DeleteMapping("/file")
    public String deleteFile(
            // todo: заменить имя юзера в кверистринг на авторизацию по токену
            @RequestParam String userName,

            @NotNull(message = "Имя файла не указано. Необходимо указать.")
            @RequestParam String filename) throws Exception {
        // todo: заменить id = 999 на автоматическую генерацию
        fileService.deleteFile(999, userName, filename);
        return "Success deleted";
    }

    @GetMapping("/file")
    public ResponseEntity<MultiValueMap<String, String>> getFile(
            // todo: заменить имя юзера в кверистринг на авторизацию по токену
            @RequestParam String userName,
            @NotNull(message = "Имя файла не указано. Необходимо указать.")
            @RequestParam String filename) throws Exception {
        // todo: заменить id = 999 на автоматическую генерацию
        MultiValueMap<String, String> body = fileService.getFile(999, userName, filename);
        return ResponseEntity.
                ok().
                header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE).
                body(body);
    }

    @PutMapping("/file")
    public String putFileName(
            // todo: заменить имя юзера в кверистринг на авторизацию по токену
            @RequestParam String userName,
            @NotEmpty @RequestParam(name = "filename") String editedFileName,
            @NotNull @RequestBody JsonName jsonName
    ) throws CustomException {
        fileService.editFileName(999, userName, editedFileName, jsonName.getName());
        return "Success upload";
    }

    @GetMapping("/list")
    public Object getFileList(
            // todo: заменить имя юзера в кверистринг на авторизацию по токену
            @RequestParam String userName,

            @NotNull(message = "Ошибка: кол-во файлов, выводимое за раз не указано. Необходимо указать.")
            @Positive(message = "Ошибка: кол-во файлов, выводимое за раз, должно быть больше нуля.")
            @Digits(integer = 100, fraction = 0, message = "Ошибка: кол-во файлов, выводимое за раз, должно быть целое число.")
            @RequestParam int limit
    ) throws CustomException {
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(fileService.getFileList(999, userName));
    }
}