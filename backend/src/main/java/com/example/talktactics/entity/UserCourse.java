package com.example.talktactics.entity;

import com.example.talktactics.common.CommonEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "user_courses")
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

    @OneToMany(mappedBy = "userCourse",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<UserCourseItem> userCourseItems;
}
