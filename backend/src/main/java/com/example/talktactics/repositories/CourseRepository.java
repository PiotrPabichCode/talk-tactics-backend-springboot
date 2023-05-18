package com.example.talktactics.repositories;

import com.example.talktactics.models.Course;
import com.example.talktactics.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE LOWER(c.level) LIKE LOWER(CONCAT('%', :levelName, '%'))")
    ArrayList<Course> findByLevelNameContaining(@Param("levelName") String levelName);
}