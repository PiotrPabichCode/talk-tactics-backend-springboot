package com.example.talktactics.repositories;

import com.example.talktactics.models.UserCourseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCourseItemRepository extends JpaRepository<UserCourseItem, Long> {
}