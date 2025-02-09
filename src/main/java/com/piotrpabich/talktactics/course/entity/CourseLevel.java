package com.piotrpabich.talktactics.course.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum CourseLevel {
    BEGINNER(0),
    INTERMEDIATE(1),
    ADVANCED(2);

    private final int value;
    public static CourseLevel fromString(final String value) {
        return switch (value) {
            case "BEGINNER" -> BEGINNER;
            case "INTERMEDIATE" -> INTERMEDIATE;
            case "ADVANCED" -> ADVANCED;
            default -> throw new IllegalArgumentException("Invalid value: " + value);
        };
    }
}
