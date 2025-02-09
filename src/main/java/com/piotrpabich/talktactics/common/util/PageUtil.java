package com.piotrpabich.talktactics.common.util;

import com.piotrpabich.talktactics.common.PageResult;
import org.springframework.data.domain.Page;

import java.util.Map;

public class PageUtil {

    public static <T> PageResult<T> toPage(final Page<T> page) {
        return new PageResult<>(page.getContent(), page.getTotalElements(), page.getTotalPages());
    }
    public static <T> PageResult<T> toPage(final Page<T> page, final Map<?, ?> contentMeta) {
        return new PageResult<>(page.getContent(), page.getTotalElements(), page.getTotalPages(), contentMeta);
    }
}
