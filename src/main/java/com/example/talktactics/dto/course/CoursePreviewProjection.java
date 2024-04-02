package com.example.talktactics.dto.course;

import com.example.talktactics.entity.CourseLevel;
import org.springframework.beans.factory.annotation.Value;

public interface CoursePreviewProjection {
    Long getId();
    String getTitle();
    String getDescription();
    CourseLevel getLevel();
    @Value("#{target.courseItemsSize}")
    int getQuantity();
}
