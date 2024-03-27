package com.example.talktactics.repository;

import com.example.talktactics.entity.UserCourseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCourseItemRepository extends JpaRepository<UserCourseItem, Long> {
    List<UserCourseItem> findAllByUserCourseCourseIdAndUserCourseUserId(Long courseId, Long userId);
}