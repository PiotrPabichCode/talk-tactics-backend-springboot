package com.example.talktactics.util;

import com.example.talktactics.common.PageResult;
import org.springframework.data.domain.Page;

public class PageUtil {

    public static <T> PageResult<T> toPage(Page<T> page) {
        return new PageResult<>(page.getContent(), page.getTotalElements(), page.getTotalPages());
    }
}
