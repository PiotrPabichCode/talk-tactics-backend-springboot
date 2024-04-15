package com.example.talktactics.dto.user_course.res;

import com.example.talktactics.dto.course.CoursePreviewDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserCourseResponseDto extends CoursePreviewDto {
    public boolean completed;
    private double progress;
}
