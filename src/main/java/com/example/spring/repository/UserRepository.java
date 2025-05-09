package com.example.spring.repository;

import com.example.spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // findBy 까지는 규칙 Username 문법
    // select * from user where username = 1?
    public User findByUsername(String username); // JPA Query Method
}
