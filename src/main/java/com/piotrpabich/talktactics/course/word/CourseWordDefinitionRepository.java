package com.piotrpabich.talktactics.course.word;

import com.piotrpabich.talktactics.course.word.entity.CourseWordDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseWordDefinitionRepository extends JpaRepository<CourseWordDefinition, Long> {
}