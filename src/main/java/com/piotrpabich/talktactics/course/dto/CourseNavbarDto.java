package com.piotrpabich.talktactics.course.dto;

import jakarta.persistence.Tuple;

public record CourseNavbarDto(
        Long id,
        String title,
        String level,
        Integer quantity
) {
    public static CourseNavbarDto fromTuple(final Tuple tuple) {
        return new CourseNavbarDto(
                tuple.get("id", Long.class),
                tuple.get("title", String.class),
                tuple.get("level", String.class),
                tuple.get("quantity", Integer.class)
        );
    }
}
