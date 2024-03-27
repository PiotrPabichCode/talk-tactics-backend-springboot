package com.example.talktactics.dto.user_course;

import com.example.talktactics.entity.Course;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseUserDto {
    Course course;
    String username;
}

