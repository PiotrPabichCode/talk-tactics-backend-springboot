package com.piotrpabich.talktactics.repository;

import com.piotrpabich.talktactics.entity.UserCourseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserCourseItemRepository extends JpaRepository<UserCourseItem, Long>, JpaSpecificationExecutor<UserCourseItem> {
}