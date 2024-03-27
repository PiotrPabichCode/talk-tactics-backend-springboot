package com.example.talktactics.dto.course;

import com.example.talktactics.entity.CourseLevel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private int id;
    private String title;
    private String description;
    private CourseLevel level;
    private int quantity;
}
