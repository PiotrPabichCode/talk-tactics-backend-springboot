package com.piotrpabich.talktactics.course_item;

import com.piotrpabich.talktactics.course_item.entity.CourseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CourseItemRepository extends JpaRepository<CourseItem, Long>, JpaSpecificationExecutor<CourseItem> {
    Optional<CourseItem> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);
}