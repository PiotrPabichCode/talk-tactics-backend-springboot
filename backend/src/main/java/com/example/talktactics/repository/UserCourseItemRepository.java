package com.example.talktactics.repository;

import com.example.talktactics.entity.UserCourseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCourseItemRepository extends JpaRepository<UserCourseItem, Long> {
}