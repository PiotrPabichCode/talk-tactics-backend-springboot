package com.example.talktactics.repository;

import com.example.talktactics.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeaningRepository extends JpaRepository<Meaning, Long> {
}