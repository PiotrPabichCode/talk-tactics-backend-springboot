package com.piotrpabich.talktactics.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user_course_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserCourseItem extends CommonEntity {

    @JsonProperty("course_item")
    @ManyToOne
    @JoinColumn(name = "course_item_id")
    private CourseItem courseItem;
    @JsonProperty("is_learned")
    private boolean isLearned = false;

    @JsonProperty("user_course")
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_course_id")
    private UserCourse userCourse;
}