package com.piotrpabich.talktactics.listeners;

import com.piotrpabich.talktactics.entity.Course;
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