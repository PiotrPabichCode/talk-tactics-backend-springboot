package com.example.talktactics.dto.user_course;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseRequestDto {
    private String courseName;
    private String login;
}
