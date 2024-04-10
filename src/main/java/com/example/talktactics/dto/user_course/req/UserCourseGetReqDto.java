package com.example.talktactics.dto.user_course.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseGetReqDto {
    @JsonProperty("course_id")
    private Long courseId;
    @JsonProperty("user_id")
    private Long userId;
}
