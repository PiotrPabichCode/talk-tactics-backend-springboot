package com.piotrpabich.talktactics.user_course.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record UserCourseDeleteReqDto(
        @NotNull
        @JsonProperty("course_id")
        Long courseId,
        @NotNull
        @JsonProperty("user_id")
        Long userId
) {
}
