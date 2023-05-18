package com.example.talktactics.repositories;

import com.example.talktactics.models.Task;
import com.example.talktactics.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.login LIKE %:userName%")
    ArrayList<User> findByUserNameContaining(@Param("userName") String userName);
    boolean existsByLogin(String login);
    User findByLogin(String login);
}