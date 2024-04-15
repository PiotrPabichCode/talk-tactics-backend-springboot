package com.example.talktactics.dto.course;

import com.example.talktactics.entity.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class CoursePreviewDto {
    private long id;
    private String title;
    private String description;
    private CourseLevel level;
    private int quantity;
}
