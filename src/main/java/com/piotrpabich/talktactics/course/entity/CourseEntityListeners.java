package com.piotrpabich.talktactics.course.entity;

import jakarta.persistence.PreUpdate;

public class CourseEntityListeners {
    @PreUpdate
    public void beforeUpdate(final Course course) {
        if(course.getCourseItems() != null) {
            course.setQuantity(course.getCourseItems().size());
        }
    }
}
