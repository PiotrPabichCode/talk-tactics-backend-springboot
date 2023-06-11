package com.example.talktactics.repositories;

import com.example.talktactics.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeaningRepository extends JpaRepository<Meaning, Long> {

    @Query("SELECT m FROM Meaning m WHERE m.courseItem IN :courseItems")
    List<Meaning> findAllByCourseItemIn(List<CourseItem> courseItems);
}