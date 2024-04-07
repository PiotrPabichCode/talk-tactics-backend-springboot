package com.example.talktactics.repository;

import com.example.talktactics.entity.CourseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseItemRepository extends JpaRepository<CourseItem, Long> {
    List<CourseItem> findByCourseId(long id);
}