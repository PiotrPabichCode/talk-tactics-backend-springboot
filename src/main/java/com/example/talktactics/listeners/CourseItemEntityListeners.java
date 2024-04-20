package com.example.talktactics.listeners;

import com.example.talktactics.entity.CourseItem;
import jakarta.persistence.PrePersist;

public class CourseItemEntityListeners {

    @PrePersist
    public void beforeSave(CourseItem courseItem) {
        if(courseItem.getLevel() == null && courseItem.getCourse() != null) {
            courseItem.setLevel(courseItem.getCourse().getLevel());
        }
    }

}
