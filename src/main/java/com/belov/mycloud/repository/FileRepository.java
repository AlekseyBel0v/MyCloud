package com.belov.mycloud.repository;

import com.belov.mycloud.model.entity.UserFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<UserFile, Long> {
    List<UserFile> findAllByUserNameAndDeletedFalseOrderByName(String userName);

    Optional<UserFile> findByNameAndDeletedFalseAndUserName(String name, String userName);

//    Page<UserFile> findAllByUserNameAndDeletedFalseOrderByName(String name);
//    Page<UserFile> findAllByUserNameAndDeletedFalseOrderByName(String userName, PageRequest of);
}