package com.example.talktactics.DTOs;

import com.example.talktactics.models.Course;
import com.example.talktactics.models.User;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseUser {
    Course course;
    String login;
}

