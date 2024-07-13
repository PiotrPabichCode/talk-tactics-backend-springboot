package com.piotrpabich.talktactics.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record PageResult<T>(List<T> content, long totalElements, long totalPages, Map<?, ?> contentMeta) {
    public PageResult(List<T> content, long totalElements, long totalPages) {
        this(content, totalElements, totalPages, new HashMap<>());
    }
}
