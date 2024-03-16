package com.belov.mycloud.service;

import com.belov.mycloud.model.front.FileDescription;
import com.belov.mycloud.model.entity.User;
import com.belov.mycloud.model.entity.UserFile;
import com.belov.mycloud.exception.CustomException;
import com.belov.mycloud.repository.FileRepository;
import com.belov.mycloud.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.nio.file.Paths.get;

/*
 * Данный сервис реализует работу с файлами, при условии, что файлы хранятся следующим образом:
 * 1. У каждого пользователя имеется на сервере одна корневая папка.
 * 2. Все файлы одного пользователя хранятся в корневой папке.
 * Так же условно считается, что состояние БД файловой системы совпадает с состоянием памяти на диске.
 */

@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;

    private final UserRepository userRepository;

    @Transactional
    public void editFileName(int idOperation, String userName, String editedFileName, String newFileName) throws CustomException {
        //проверим, существует ли пользователь с именем fileName
        User user = userRepository.findByName(userName);    //todo: заменить на проверку токена
        try {
            Optional<UserFile> fileFromRepo = fileRepository.findByNameAndDeletedFalseAndUserName(editedFileName, userName);
            if (fileFromRepo.isEmpty()) {
                throw new CustomException(String.format("Ошибка: файл с именем %s не найден", editedFileName), idOperation, HttpStatus.BAD_REQUEST);
            }
            UserFile file = fileFromRepo.get();
            // переименовывание файла на диске
            Path path = getFilePath(file);    // путь к файлу на диске со старым именем
            Path deletedFile = Path.of(path.getParent().toString(), newFileName);    // путь к файлу на диске с новым именем
            if (!Files.isWritable(path)) {  //проверка доступа к файлу на диске
                //todo: в зависимости от того, кто блокирует файл, статус ошибки будет иметь код 400 или 500. Для упрощения принято 400.
                throw new CustomException("This file is not available for delete. Try later", idOperation, HttpStatus.BAD_REQUEST);
            }
            Files.move(path, deletedFile, StandardCopyOption.REPLACE_EXISTING);
            file.setName(newFileName);  //переименовываем файл в репозитории
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException("Error upload file", idOperation, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Transactional
    public List<FileDescription> getFileList(int idOperation, String userName) throws CustomException {
        //проверим, существует ли пользователь с именем fileName
        User user = userRepository.findByName(userName);    //todo: заменить на проверку токена
        try {
            List<UserFile> fileList = fileRepository.findAllByUserNameAndDeletedFalseOrderByName(userName);
            //todo: проверить, какой будет ли равен список null, если файлов нет
            return fileList.stream().map(x -> new FileDescription(x.getName(), x.getFileSize())).toList();
        } catch (Exception e) {
            throw new CustomException("Error of getting file list", idOperation, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


    @Transactional
    public void saveFile(int idOperation, String userName, String fileName, String hash, MultipartFile file) throws CustomException, IOException, NoSuchAlgorithmException {
        //Метод saveFile сохраняет файл на диске, если файла с таким именем не существует
//        if (file.isEmpty()){
//            throw new CustomException("Файл отсутствует. Необходимо добавить файл.", idOperation, HttpStatus.BAD_REQUEST);
//        }
        //проверим, существует ли пользователь с именем fileName
        User user = userRepository.findByName(userName);    //todo: заменить на проверку токена
        try {
            Optional<UserFile> fileFromRepo = fileRepository.findByNameAndDeletedFalseAndUserName(fileName, userName);
            if (fileFromRepo.isEmpty()) {
                //запись файла на диск
                Path fileFromDisk = Paths.get(user.getPath(), fileName);
                fileFromDisk.toFile().createNewFile();
                file.transferTo(fileFromDisk.toFile());
                if (!HashService.checkHash(fileFromDisk, hash)) {  //проверка hash
                    throw new CustomException("Error save file. Try again.", idOperation, HttpStatus.CONFLICT);
                }
                //сохраняю информацию о файле в базе
                fileRepository.save(UserFile.builder().user(user).fileHash(hash).name(fileName).fileSize(file.getSize()).build());
            } else {
                throw new CustomException(String.format("A file with name %s already exists. Change the file name and try again", fileName), idOperation, HttpStatus.BAD_REQUEST);
            }
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException("Error save file", idOperation, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Transactional
    public void deleteFile(int idOperation, String userName, String fileName) throws Exception {
        User user = userRepository.findByName(userName);    //todo: заменить на проверку токена
        //проверим, существует ли пользователь с именем fileName
        try {
            Optional<UserFile> fileFromRepo = fileRepository.findByNameAndDeletedFalseAndUserName(fileName, userName);
            if (fileFromRepo.isEmpty()) {   //существует ли файл в базе
                throw new CustomException(String.format("File with name %s wasn't found", fileName), idOperation, HttpStatus.BAD_REQUEST);
            } else {
                //вместо удаления файла дальше выполняется его переименовывание
                Path path = getFilePath(fileFromRepo.get());    // путь к файлу на диске со старым именем
                Path deletedFile = Path.of(path +          // путь к файлу на диске с новым именем
                        "_deleted_on_" +
                        new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SS").format(new Date()));
                if (!Files.isWritable(path)) {  //проверка доступа к файлу на диске
                    //todo: в зависимости от того, кто блокирует файл, статус ошибки будет иметь код 400 или 500. Для упрощения принято 400.
                    throw new CustomException("This file is not available for delete. Try later", idOperation, HttpStatus.BAD_REQUEST);
                }
                Files.move(path, deletedFile, StandardCopyOption.REPLACE_EXISTING);  //переименовываем файл на диске
                fileFromRepo.get().setDeleted(true);    //в таблице помечаем, как удаленный (для упрощения имя не меняется)
            }
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException("Error delet file", idOperation, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Transactional
    public MultiValueMap<String, String> getFile(int idOperation, String userName, String fileName) throws CustomException {
        User user = userRepository.findByName(userName);    //todo: заменить на проверку токена
        //проверим, существует ли пользователь с именем fileName
        try {
            Optional<UserFile> fileFromRepo = fileRepository.findByNameAndDeletedFalseAndUserName(fileName, userName);
            if (fileFromRepo.isEmpty()) {   //существует ли файл в базе
                throw new CustomException("This file wasn't found", idOperation, HttpStatus.BAD_REQUEST);
            }
            Path path = getFilePath(fileFromRepo.get());
            if (!Files.isReadable(path)) {  //проверка доступа к файлу на диске
                //todo: в зависимости от того, кто блокирует файл, статус ошибки будет иметь код 400 или 500. Для упрощения принято 400.
                throw new CustomException("This file is not available. Try later", idOperation, HttpStatus.BAD_REQUEST);
            }
            byte[] bytes = Files.readAllBytes(path);
            MultiValueMap<String, String> responseBody = new LinkedMultiValueMap<>();
            responseBody.add("hash", fileFromRepo.get().getFileHash());
//            responseBody.add("file", Base64.getEncoder().encodeToString(bytes));
            responseBody.add("file", new String(bytes, StandardCharsets.UTF_8));
            return responseBody;
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException("Error upload file. Try later", idOperation, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //возвращает путь к файлу на диске
    private Path getFilePath(@NotNull UserFile userFile) {
        return Path.of(userFile.getUser().getPath(), userFile.getName());
    }

    //    //Предполагалось, что этот метод выводит список файлов частями по limit штук за раз
//    @Transactional
//    public List<FileDescription> getFileList(int idOperation, String userName, int limit) throws CustomException {
//        //проверим, существует ли пользователь с именем fileName
//        User user = userRepository.findByName(userName);    //todo: заменить на проверку токена
//        try {
//            Page<UserFile> pages = fileRepository.findAllByUserNameAndDeletedFalseOrderByName(userName, PageRequest.of(0, limit));
//            pages.
//            //todo: проверить, какой будет ли равен список null, если файлов нет
//
//        } catch (CustomException ce) {
//            throw ce;
//        } catch (Exception e) {
//            throw new CustomException("Error getting file list", idOperation, HttpStatus.INTERNAL_SERVER_ERROR, e);
//        }
//        return new ArrayList<>();
//    }
}