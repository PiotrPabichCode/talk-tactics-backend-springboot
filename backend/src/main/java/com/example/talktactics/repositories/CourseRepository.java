package com.example.talktactics.repositories;

import com.example.talktactics.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}