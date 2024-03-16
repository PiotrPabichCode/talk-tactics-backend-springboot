package com.example.talktactics.entity;

import com.example.talktactics.common.CommonEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "user_course_items")
public class UserCourseItem extends CommonEntity {

    @JsonProperty("course_item")
    @ManyToOne
    @JoinColumn(name = "course_item_id")
    private CourseItem courseItem;
    @JsonProperty("is_learned")
    private boolean isLearned;

    @JsonProperty("user_course")
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_course_id")
    private UserCourse userCourse;
}