package com.example.talktactics.entity;

import com.example.talktactics.common.CommonEntity;
import com.example.talktactics.dto.course.CoursePreviewDto;
import com.example.talktactics.listeners.CourseEntityListeners;
import com.example.talktactics.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "courses")
@EntityListeners(CourseEntityListeners.class)
public class Course extends CommonEntity {
    private String title;
    @Column(length = 800)
    private String description;
    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    @JsonIgnore
    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CourseItem> courseItems;

    private int quantity = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<UserCourse> userCourses;

    public CoursePreviewDto toCoursePreviewDto() {
        return new CoursePreviewDto(this.getId(), this.getTitle(), this.getDescription(), this.getLevel(), this.getQuantity());
    }

    public int getPoints() {
        return switch (this.level) {
            case BEGINNER -> Constants.BEGINNER_COMPLETED_POINTS;
            case INTERMEDIATE -> Constants.INTERMEDIATE_COMPLETED_POINTS;
            case ADVANCED -> Constants.ADVANCED_COMPLETED_POINTS;
        };
    }
}
