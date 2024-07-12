package com.example.talktactics.repository;

import com.example.talktactics.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

}