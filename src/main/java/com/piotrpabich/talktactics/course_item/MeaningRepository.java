package com.piotrpabich.talktactics.course_item;

import com.piotrpabich.talktactics.course_item.entity.Meaning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeaningRepository extends JpaRepository<Meaning, Long> {
}