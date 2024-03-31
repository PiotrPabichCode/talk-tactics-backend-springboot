package com.example.talktactics.dto;

import com.example.talktactics.entity.CourseLevel;

public interface CoursePreviewProjection {
    Long getId();
    String getTitle();
    String getDescription();
    CourseLevel getLevel();
    int getCourseItemsSize(); // This might need a custom query to calculate
}
