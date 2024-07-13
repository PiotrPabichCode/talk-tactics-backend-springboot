package com.piotrpabich.talktactics.repository;

import com.piotrpabich.talktactics.entity.CourseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CourseItemRepository extends JpaRepository<CourseItem, Long>, JpaSpecificationExecutor<CourseItem> {
    List<CourseItem> findByCourseId(long id);
}