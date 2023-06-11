package com.example.talktactics.repositories;

import com.example.talktactics.models.UserCourseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserCourseItemRepository extends JpaRepository<UserCourseItem, Long> {

}