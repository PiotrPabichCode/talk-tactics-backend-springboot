package com.piotrpabich.talktactics.repository;

import com.piotrpabich.talktactics.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long>, JpaSpecificationExecutor<UserCourse> {
    boolean existsByCourseIdAndUserId(long courseId, long userId);
    Optional<UserCourse> findByCourseIdAndUserId(long courseId, long userId);
}