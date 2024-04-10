package com.example.talktactics.dto.user_course.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseAddReqDto {
    @JsonProperty("course_id")
    private long courseId;
    @JsonProperty("user_id")
    private long userId;
}
