package com.piotrpabich.talktactics.course.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.course.CourseConstants;
import com.piotrpabich.talktactics.course_item.entity.CourseItem;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "courses")
@EntityListeners(CourseEntityListeners.class)
public class Course extends CommonEntity {
    @NotBlank(message = "Cannot be blank")
    private String title;
    @Column(length = 800)
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    @JsonIgnore
    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CourseItem> courseItems;

    private int quantity;

    @JsonIgnore
    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<UserCourse> userCourses;

    public int getPoints() {
        return switch (this.level) {
            case BEGINNER -> CourseConstants.BEGINNER_COMPLETED_POINTS;
            case INTERMEDIATE -> CourseConstants.INTERMEDIATE_COMPLETED_POINTS;
            case ADVANCED -> CourseConstants.ADVANCED_COMPLETED_POINTS;
        };
    }
}
