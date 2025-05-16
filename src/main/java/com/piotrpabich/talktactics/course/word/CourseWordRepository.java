package com.piotrpabich.talktactics.course.word;

import com.piotrpabich.talktactics.course.word.entity.CourseWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CourseWordRepository extends JpaRepository<CourseWord, Long>, JpaSpecificationExecutor<CourseWord> {
    Optional<CourseWord> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    @Query(value = "SELECT cw.uuid FROM course_words cw JOIN courses c ON cw.course_id = c.id " +
            "WHERE c.uuid = :courseUuid ORDER BY RANDOM() LIMIT 1",
            nativeQuery = true)
    UUID findRandomUuidByCourseUuidNative(UUID courseUuid);
}