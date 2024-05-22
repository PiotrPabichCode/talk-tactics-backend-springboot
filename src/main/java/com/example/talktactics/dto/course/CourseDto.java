package com.example.talktactics.dto.course;

import com.example.talktactics.entity.CourseLevel;
import lombok.Data;

import java.io.Serializable;

@Data
public class CourseDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private CourseLevel level;
    private int quantity;
    private int points;
}
