package com.piotrpabich.talktactics.course_item;

import com.piotrpabich.talktactics.course_item.entity.CourseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseItemRepository extends JpaRepository<CourseItem, Long>, JpaSpecificationExecutor<CourseItem> {
}