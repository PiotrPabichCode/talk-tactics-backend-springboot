package com.piotrpabich.talktactics.course.participant.word;

import com.piotrpabich.talktactics.course.participant.word.entity.CourseParticipantWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CourseParticipantWordRepository extends JpaRepository<CourseParticipantWord, Long>, JpaSpecificationExecutor<CourseParticipantWord> {
    Optional<CourseParticipantWord> findByUuid(UUID userCourseItemUuid);
}