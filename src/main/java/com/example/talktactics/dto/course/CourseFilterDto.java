package com.example.talktactics.dto.course;

import com.example.talktactics.entity.CourseLevel;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CourseFilterDto {
    private String title;
    private String description;
    private Set<CourseLevel> levels;
    private Integer minQuantity;
    private Integer maxQuantity;
}
