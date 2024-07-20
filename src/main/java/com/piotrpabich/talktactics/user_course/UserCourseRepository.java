package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long>, JpaSpecificationExecutor<UserCourse> {
    boolean existsByCourseIdAndUserId(long courseId, long userId);
    Optional<UserCourse> findByCourseIdAndUserId(long courseId, long userId);
}