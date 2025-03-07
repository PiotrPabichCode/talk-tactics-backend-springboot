package com.piotrpabich.talktactics.course.word;

import com.piotrpabich.talktactics.course.word.entity.CourseWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CourseWordRepository extends JpaRepository<CourseWord, Long>, JpaSpecificationExecutor<CourseWord> {
    Optional<CourseWord> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);
}