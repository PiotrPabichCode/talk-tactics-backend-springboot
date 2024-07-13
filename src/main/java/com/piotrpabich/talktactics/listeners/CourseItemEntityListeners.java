package com.piotrpabich.talktactics.listeners;

import com.piotrpabich.talktactics.entity.CourseItem;
import jakarta.persistence.PrePersist;
import lombok.NonNull;

public class CourseItemEntityListeners {

    @PrePersist
    public void beforeSave(@NonNull CourseItem courseItem) {
        if(courseItem.getLevel() == null && courseItem.getCourse() != null) {
            courseItem.setLevel(courseItem.getCourse().getLevel());
        }
    }

}
