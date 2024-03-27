package com.example.talktactics.dto.user_course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCoursePreviewDto {
    int id;
    @JsonProperty("user_id")
    int userId;
    @JsonProperty("course_id")
    int courseId;
    double progress;
    boolean completed;
}
