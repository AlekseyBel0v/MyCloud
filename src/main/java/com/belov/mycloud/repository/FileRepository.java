package com.belov.mycloud.repository;

import com.belov.mycloud.domain.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<UserFile, Long> {
    Optional<UserFile> findByNameAndDeletedFalseAndUserName(String name, String userName);
}