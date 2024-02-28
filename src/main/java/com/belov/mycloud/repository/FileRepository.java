package com.belov.mycloud.repository;

import com.belov.mycloud.domain.UserFile;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<UserFile, Long> {
    public Optional<UserFile> findByNameAndDeletedFalseAndUserName(@NotNull String name, @NotNull String userName);
}
