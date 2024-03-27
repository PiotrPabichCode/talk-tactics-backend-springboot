package com.example.talktactics.dto.user_course_item.res;

import com.example.talktactics.dto.user_course_item.UserCourseItemPreviewDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class GetUserCourseItemPreviewDtoResponse {
    @JsonProperty("course_name")
    String courseName;
    List<UserCourseItemPreviewDto> items;
}
