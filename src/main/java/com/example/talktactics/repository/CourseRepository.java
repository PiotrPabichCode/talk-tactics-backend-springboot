package com.example.talktactics.repository;

import com.example.talktactics.dto.course.CourseNavbarDto;
import com.example.talktactics.dto.course.CoursePreviewProjection;
import com.example.talktactics.entity.Course;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    Course findByTitle(String title);
    @Query(nativeQuery = true, value = "SELECT id, title, level, quantity " +
            "FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY level ORDER BY random()) AS row_num " +
            "FROM courses) AS RankedCourses " +
            "WHERE row_num = 1")
    List<Tuple> findNavbarList();

    @Query("SELECT c.id as id, c.title as title, c.description as description, c.level as level, size(c.courseItems) as courseItemsSize FROM Course c ORDER BY c.id")
    List<CoursePreviewProjection> findCoursePreviews();
}