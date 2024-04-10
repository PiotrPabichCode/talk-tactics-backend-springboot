package com.example.talktactics.dto.user_course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCoursePreviewDto {
    long id;
    @JsonProperty("user_id")
    long userId;
    @JsonProperty("course_id")
    long courseId;
    double progress;
    boolean completed;
}
