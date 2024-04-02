package com.example.talktactics.repository;

import com.example.talktactics.dto.course.CoursePreviewProjection;
import com.example.talktactics.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByTitle(String title);
    default List<Course> findByLevelName(String substring) {
        return findAll().stream()
                .filter(course -> course.getLevel().toString().toLowerCase().contains(substring.toLowerCase())).toList();
    }

    @Query("SELECT c.id as id, c.title as title, c.description as description, c.level as level, size(c.courseItems) as courseItemsSize FROM Course c ORDER BY c.id")
    List<CoursePreviewProjection> findCoursePreviews();
}