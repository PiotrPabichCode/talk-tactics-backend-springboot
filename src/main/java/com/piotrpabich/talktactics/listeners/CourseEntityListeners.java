package com.piotrpabich.talktactics.listeners;

import com.piotrpabich.talktactics.entity.Course;
import jakarta.persistence.PreUpdate;

public class CourseEntityListeners {
    @PreUpdate
    public void beforeUpdate(Course course) {
        if(course.getCourseItems() != null) {
            course.setQuantity(course.getCourseItems().size());
        }
    }
}
