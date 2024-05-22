package com.example.talktactics.common;

import java.util.List;

public record PageResult<T>(List<T> content, long totalElements) {
}
