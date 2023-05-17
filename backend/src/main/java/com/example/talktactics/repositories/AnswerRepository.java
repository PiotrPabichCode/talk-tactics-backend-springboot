package com.example.talktactics.repositories;

import com.example.talktactics.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("SELECT a FROM Answer a WHERE a.user.login LIKE %:login%")
    ArrayList<Answer> findByUserNameContaining(@Param("login") String login);

}