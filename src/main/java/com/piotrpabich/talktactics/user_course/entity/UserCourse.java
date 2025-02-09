package com.piotrpabich.talktactics.user_course.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.piotrpabich.talktactics.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "user_courses")
@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder(toBuilder = true)
@EntityListeners(UserCourseEntityListeners.class)
public class UserCourse extends CommonEntity {

    private double progress;
    @JsonProperty("is_completed")
    private boolean completed;
    private int points;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @JsonIgnore
    @OneToMany(mappedBy = "userCourse",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<UserCourseItem> userCourseItems;
}
