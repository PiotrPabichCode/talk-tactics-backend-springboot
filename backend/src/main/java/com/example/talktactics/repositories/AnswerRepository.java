package com.example.talktactics.repositories;

import com.example.talktactics.models.Answer;
import com.example.talktactics.models.Course;
import com.example.talktactics.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("SELECT a FROM Answer a WHERE a.user.login LIKE %:login%")
    ArrayList<Answer> findByUserNameContaining(@Param("login") String login);

    @Query("SELECT a FROM Answer a WHERE a.task IN :tasks")
    List<Answer> findByTaskIn(List<Task> tasks);
}