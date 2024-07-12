package com.example.talktactics.dto.user_course.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserCourseAddReqDto(
        @JsonProperty("course_id")
        Long courseId,
        @JsonProperty("user_id")
        Long userId
) {
}
