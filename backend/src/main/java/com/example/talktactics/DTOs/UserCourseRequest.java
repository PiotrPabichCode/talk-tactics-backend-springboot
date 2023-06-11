package com.example.talktactics.DTOs;

import com.example.talktactics.models.User;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseRequest {
    private String courseName;
    private String login;
}
