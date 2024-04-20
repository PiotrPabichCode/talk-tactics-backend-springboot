package com.example.talktactics.entity;

import com.example.talktactics.common.CommonEntity;
import com.example.talktactics.dto.user_course_item.UserCourseItemPreviewDto;
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
    private boolean isLearned;

    @JsonProperty("user_course")
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_course_id")
    private UserCourse userCourse;

    public UserCourseItemPreviewDto toUserCourseItemPreviewDto() {
        return new UserCourseItemPreviewDto(this.getId(), this.getCourseItem().getId(), this.getCourseItem().getWord(), this.getCourseItem().getPartOfSpeech(), this.getCourseItem().getPhonetic(), this.isLearned);
    }
}
