package com.example.talktactics.repositories;

import com.example.talktactics.models.CourseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseItemRepository extends JpaRepository<CourseItem, Long> {
    List<CourseItem> findByCourseId(int id);
}