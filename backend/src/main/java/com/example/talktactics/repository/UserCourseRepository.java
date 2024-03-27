package com.example.talktactics.repository;

import com.example.talktactics.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    boolean existsByCourseTitleAndUserUsername(String title, String username);
    boolean existsByCourseIdAndUserId(Long courseId, Long userId);
    UserCourse findByCourseTitleAndUserUsername(String title, String username);
    UserCourse findByCourseIdAndUserId(Long courseId, Long userId);

}