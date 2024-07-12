package com.example.talktactics.util;

import org.springframework.data.domain.Sort;

public class SortUtil {

    public static String getSortProperty(Sort sort) {
        return sort.stream().findFirst().map(Sort.Order::getProperty).orElse(null);
    }

    public static Sort.Direction getSortDirection(Sort sort) {
        return sort.stream().findFirst().map(Sort.Order::getDirection).orElse(null);
    }

    public static boolean isSortAscending(Sort sort) {
        return SortUtil.getSortDirection(sort).equals(Sort.Direction.ASC);
    }
}
