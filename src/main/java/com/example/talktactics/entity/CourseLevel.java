package com.example.talktactics.entity;

import java.util.HashMap;
import java.util.Map;

public enum CourseLevel {
    BEGINNER((short) 0),
    INTERMEDIATE((short) 1),
    ADVANCED((short) 2);

    private final short value;
    private static final Map<Short, CourseLevel> map = new HashMap<>();

    static {
        for (CourseLevel level : CourseLevel.values()) {
            map.put(level.value, level);
        }
    }

    CourseLevel(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public static CourseLevel fromShort(short value) {
        return map.get(value);
    }

    public static CourseLevel fromString(String value) {
        return switch (value) {
            case "BEGINNER" -> BEGINNER;
            case "INTERMEDIATE" -> INTERMEDIATE;
            case "ADVANCED" -> ADVANCED;
            default -> throw new IllegalArgumentException("Invalid value: " + value);
        };
    }
}
