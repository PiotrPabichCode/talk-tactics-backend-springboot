package com.example.talktactics.repository;

import com.example.talktactics.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByName(String name);
    default List<Course> findByLevelName(String substring) {
        return findAll().stream()
                .filter(course -> course.getLevel().toString().toLowerCase().contains(substring.toLowerCase()))
                .collect(Collectors.toList());
    }
}