package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long>, JpaSpecificationExecutor<UserCourse> {

    boolean existsByCourseUuidAndUserUuid(UUID courseUuid, UUID userUuid);

    Optional<UserCourse> findByCourseUuidAndUserUuid(UUID courseUuid, UUID userUuid);

    Optional<UserCourse> findByUuid(UUID userCourseUuid);
}