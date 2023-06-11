package com.example.talktactics.repositories;

import com.example.talktactics.models.Course;
import com.example.talktactics.models.User;
import com.example.talktactics.models.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    List<UserCourse> findByUserId(int id);
    List<UserCourse> findByUser(User user);
    boolean existsByCourseNameAndUserLogin(String name, String login);
    UserCourse findByCourseNameAndUserLogin(String name, String login);

}