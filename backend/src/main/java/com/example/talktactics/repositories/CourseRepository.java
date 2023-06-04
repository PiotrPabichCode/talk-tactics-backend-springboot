package com.example.talktactics.repositories;

import com.example.talktactics.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE LOWER(c.level) LIKE LOWER(CONCAT('%', :levelName, '%'))")
    ArrayList<Course> findByLevelName(@Param("levelName") String levelName);
}