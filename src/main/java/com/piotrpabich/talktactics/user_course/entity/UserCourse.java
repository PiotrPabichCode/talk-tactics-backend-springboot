package com.piotrpabich.talktactics.user_course.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.piotrpabich.talktactics.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_courses")
@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder(toBuilder = true)
@EntityListeners(UserCourseEntityListeners.class)
public class UserCourse extends CommonEntity {

    public UserCourse(final User user, final Course course) {
        this.user = user;
        this.course = course;
    }

    private Double progress = 0.0;
    private Boolean completed = false;
    private Integer points = 0;

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
    private List<UserCourseItem> userCourseItems = new ArrayList<>();
}
