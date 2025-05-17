package com.piotrpabich.talktactics.course;

import com.piotrpabich.talktactics.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    @Query(nativeQuery = true, value = "SELECT DISTINCT ON (level) c.* FROM courses c ORDER BY level, RANDOM()")
    List<Course> findNavbarList();

    Optional<Course> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    void deleteByUuid(UUID courseUuid);
}