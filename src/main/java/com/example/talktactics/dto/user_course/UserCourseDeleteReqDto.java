package com.example.talktactics.dto.user_course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseDeleteReqDto {
    @JsonProperty("course_id")
    private Long courseId;
    @JsonProperty("user_id")
    private Long userId;
}
