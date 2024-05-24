package com.example.talktactics.util;

import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.Comparator;


public class SortUtil {

    @SuppressWarnings("unchecked")
    public static <T> Comparator<T> getComparator(Sort sort) {
        return (o1, o2) -> {
            for (Sort.Order order : sort) {
                int comparison;
                Comparable<Object> value1 = (Comparable<Object>) getField(o1, order.getProperty());
                Comparable<Object> value2 = (Comparable<Object>) getField(o2, order.getProperty());

                if (value1 == null && value2 == null) {
                    comparison = 0;
                } else if (value1 == null) {
                    comparison = order.getDirection() == Sort.Direction.ASC ? -1 : 1;
                } else if (value2 == null) {
                    comparison = order.getDirection() == Sort.Direction.ASC ? 1 : -1;
                } else {
                    comparison = order.getDirection() == Sort.Direction.ASC ? value1.compareTo(value2) : value2.compareTo(value1);
                }
                return comparison;
            }
            return 0;
        };
    }

    private static Object getField(Object item, String fieldName) {
        try {
            String[] fieldNames = fieldName.split("\\.");
            Object value = item;
            for (String name : fieldNames) {
                Field field = value.getClass().getDeclaredField(name);
                field.setAccessible(true);
                value = field.get(value);
            }
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
