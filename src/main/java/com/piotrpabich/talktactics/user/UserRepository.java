package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);

}