package com.example.talktactics.repository;

import com.example.talktactics.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    boolean existsByCourseNameAndUserLogin(String name, String login);
    UserCourse findByCourseNameAndUserLogin(String name, String login);

}