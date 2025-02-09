package com.piotrpabich.talktactics.course;

import com.piotrpabich.talktactics.course.entity.Course;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    @Query(nativeQuery = true, value = "SELECT id, title, level, quantity " +
            "FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY level ORDER BY random()) AS row_num " +
            "FROM courses) AS RankedCourses " +
            "WHERE row_num = 1")
    List<Tuple> findNavbarList();
}