package com.example.talktactics.repository;

import com.example.talktactics.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    boolean existsByCourseIdAndUserId(long courseId, long userId);
    UserCourse findByCourseIdAndUserId(long courseId, long userId);
    List<UserCourse> findAllByUserId(long userId);

}