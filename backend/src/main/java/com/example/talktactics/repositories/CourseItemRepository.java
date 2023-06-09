package com.example.talktactics.repositories;

import com.example.talktactics.models.CourseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseItemRepository extends JpaRepository<CourseItem, Long> {
}