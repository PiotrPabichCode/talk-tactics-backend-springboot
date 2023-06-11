package com.example.talktactics.repositories;

import com.example.talktactics.models.Course;
import com.example.talktactics.models.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByName(String name);
    List<Course> findByLevel(Level level);
    default List<Course> findByLevelName(String substring) {
        return findAll().stream()
                .filter(course -> course.getLevel().toString().toLowerCase().contains(substring.toLowerCase()))
                .collect(Collectors.toList());
    }
}