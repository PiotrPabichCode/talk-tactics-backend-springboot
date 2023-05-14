package com.example.talktactics.repositories;

import com.example.talktactics.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}