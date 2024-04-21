package com.example.talktactics.listeners;

import com.example.talktactics.entity.Course;
import jakarta.persistence.PreUpdate;
import lombok.NonNull;

public class CourseEntityListeners {
    @PreUpdate
    public void beforeUpdate(@NonNull Course course) {
        if(course.getCourseItems() != null) {
            course.setQuantity(course.getCourseItems().size());
        }
    }
}
