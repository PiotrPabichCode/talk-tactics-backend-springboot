package com.example.talktactics.listeners;

import com.example.talktactics.entity.Course;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class CourseEntityListeners {
    @PrePersist
    @PreUpdate
    public void beforeSave(Course course) {
        if(course.getCourseItems() != null) {
            course.setQuantity(course.getCourseItems().size());
        } else {
            course.setQuantity(0);
        }
    }
}
