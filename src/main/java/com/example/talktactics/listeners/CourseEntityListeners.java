package com.example.talktactics.listeners;

import com.example.talktactics.entity.Course;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class CourseEntityListeners {
    @PreUpdate
    public void beforeUpdate(Course course) {
        if(course.getCourseItems() != null) {
            course.setQuantity(course.getCourseItems().size());
        }
    }
}
