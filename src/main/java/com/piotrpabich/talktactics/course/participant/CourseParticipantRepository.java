package com.piotrpabich.talktactics.course.participant;

import com.piotrpabich.talktactics.course.participant.entity.CourseParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CourseParticipantRepository extends JpaRepository<CourseParticipant, Long>, JpaSpecificationExecutor<CourseParticipant> {

    boolean existsByCourseUuidAndUserUuid(UUID courseUuid, UUID userUuid);

    Optional<CourseParticipant> findByCourseUuidAndUserUuid(UUID courseUuid, UUID userUuid);

    Optional<CourseParticipant> findByUuid(UUID userCourseUuid);
}