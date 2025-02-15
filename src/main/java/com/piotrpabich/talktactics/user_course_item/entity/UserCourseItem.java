package com.piotrpabich.talktactics.user_course_item.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.piotrpabich.talktactics.course_item.entity.CourseItem;
import com.piotrpabich.talktactics.user_course_item.UserCourseItemListeners;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user_course_items")
@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder(toBuilder = true)
@EntityListeners(UserCourseItemListeners.class)
public class UserCourseItem extends CommonEntity {

    @ManyToOne
    @JoinColumn(name = "course_item_id")
    private CourseItem courseItem;

    private boolean isLearned;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_course_id")
    private UserCourse userCourse;
}
