package com.example.talktactics.dto.course;

import com.example.talktactics.entity.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class CourseDetailsDto {
    long id;
    String title;
    String description;
    CourseLevel level;
    int quantity;
}
