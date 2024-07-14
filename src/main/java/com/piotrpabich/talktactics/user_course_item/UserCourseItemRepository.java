package com.piotrpabich.talktactics.user_course_item;

import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserCourseItemRepository extends JpaRepository<UserCourseItem, Long>, JpaSpecificationExecutor<UserCourseItem> {
}