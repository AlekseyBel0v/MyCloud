package com.belov.mycloud.repository;

import com.belov.mycloud.domain.User;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    public User findByName(String name);
}
