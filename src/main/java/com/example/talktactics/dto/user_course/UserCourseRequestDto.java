package com.example.talktactics.dto.user_course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseRequestDto {
    @JsonProperty("course_id")
    private Long courseId;
    @JsonProperty("user_id")
    private Long userId;
}
