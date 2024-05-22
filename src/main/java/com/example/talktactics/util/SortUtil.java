package com.example.talktactics.util;

import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.Comparator;


public class SortUtil {

    @SuppressWarnings("unchecked")
    public static <T> Comparator<T> getComparator(Sort sort) {
        Comparator<T> comparator = Comparator.comparing(item -> (Comparable<Object>) getField(item, sort.iterator().next().getProperty()));

        if (sort.iterator().next().isDescending()) {
            comparator = comparator.reversed();
        }

        for (Sort.Order order : sort) {
            Comparator<T> orderComparator = Comparator.comparing(item -> (Comparable<Object>) getField(item, order.getProperty()));
            if (order.isDescending()) {
                orderComparator = orderComparator.reversed();
            }
            comparator = comparator.thenComparing(orderComparator);
        }
        return comparator;
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
