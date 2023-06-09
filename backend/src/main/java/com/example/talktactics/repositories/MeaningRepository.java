package com.example.talktactics.repositories;

import com.example.talktactics.models.Meaning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeaningRepository extends JpaRepository<Meaning, Long> {
}