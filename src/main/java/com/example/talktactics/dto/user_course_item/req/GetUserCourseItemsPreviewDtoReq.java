package com.example.talktactics.dto.user_course_item.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetUserCourseItemsPreviewDtoReq {
    @JsonProperty("user_id")
    Long userId;
    @JsonProperty("course_id")
    Long courseId;
}
