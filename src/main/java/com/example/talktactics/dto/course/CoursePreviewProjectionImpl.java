package com.example.talktactics.dto.course;

import com.example.talktactics.entity.CourseLevel;

public class CoursePreviewProjectionImpl implements CoursePreviewProjection {

    private Long id;
    private String title;
    private String description;
    private CourseLevel level;
    private int quantity;

    public CoursePreviewProjectionImpl(Long id, String title, String description, CourseLevel level, int quantity) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.level = level;
        this.quantity = quantity;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public CourseLevel getLevel() {
        return level;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }
}
