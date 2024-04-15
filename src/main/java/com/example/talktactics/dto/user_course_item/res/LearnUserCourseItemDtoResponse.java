package com.example.talktactics.dto.user_course_item.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LearnUserCourseItemDtoResponse {
    @JsonProperty("course_id")
    public long courseId;
}
