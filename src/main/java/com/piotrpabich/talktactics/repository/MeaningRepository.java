package com.piotrpabich.talktactics.repository;

import com.piotrpabich.talktactics.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeaningRepository extends JpaRepository<Meaning, Long> {
}