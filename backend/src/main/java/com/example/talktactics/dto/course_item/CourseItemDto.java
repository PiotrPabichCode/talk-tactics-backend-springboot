package com.example.talktactics.dto.course_item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseItemDto {
    int id;
    String word;
    String courseName;
}
