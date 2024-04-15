package com.example.talktactics.entity;

import com.example.talktactics.common.CommonEntity;
import com.example.talktactics.dto.user_course.UserCoursePreviewDto;
import com.example.talktactics.dto.user_course.res.UserCourseResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "user_courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserCourse extends CommonEntity {
    private double progress;
    @JsonProperty("is_completed")
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;


    @JsonIgnore
    @OneToMany(mappedBy = "userCourse",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<UserCourseItem> userCourseItems;


    public UserCoursePreviewDto toUserCoursePreviewDto() {
        return UserCoursePreviewDto.builder()
                .id(this.getId())
                .userId(this.getUser().getId())
                .courseId(this.getCourse().getId())
                .progress(this.getProgress())
                .completed(this.isCompleted())
                .build();
    }

    public UserCourseResponseDto toUserCourseResponseDto() {
        return UserCourseResponseDto.builder()
                .id(this.getCourse().getId())
                .title(this.getCourse().getTitle())
                .description(this.getCourse().getDescription())
                .level(this.getCourse().getLevel())
                .quantity(this.getCourse().getQuantity())
                .completed(this.isCompleted())
                .progress(this.getProgress())
                .build();
    }
}
