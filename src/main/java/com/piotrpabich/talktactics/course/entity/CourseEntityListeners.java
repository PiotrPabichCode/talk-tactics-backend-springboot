package com.piotrpabich.talktactics.course.entity;

import com.piotrpabich.talktactics.course.entity.Course;
import jakarta.persistence.PreUpdate;

public class CourseEntityListeners {
    @PreUpdate
    public void beforeUpdate(Course course) {
        if(course.getCourseItems() != null) {
            course.setQuantity(course.getCourseItems().size());
        }
    }
}
