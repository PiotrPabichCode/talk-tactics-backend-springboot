package com.example.talktactics.dto.user_course;

import com.example.talktactics.dto.course.CourseDetailsDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserCourseDetailsDto extends CourseDetailsDto {
    int points;
    double progress;
    boolean completed;
}
