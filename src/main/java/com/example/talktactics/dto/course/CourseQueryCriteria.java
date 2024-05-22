package com.example.talktactics.dto.course;

import com.example.talktactics.common.Query;
import com.example.talktactics.entity.CourseLevel;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CourseQueryCriteria {
    @Query(type = Query.Type.INNER_LIKE)
    private String title;
    @Query(type = Query.Type.INNER_LIKE)
    private String description;
    @Query(propName = "level", type = Query.Type.IN)
    private Set<CourseLevel> levels = new HashSet<>();
    @Query(propName = "quantity", type = Query.Type.GREATER_THAN)
    private Integer minQuantity;
    @Query(propName = "quantity", type = Query.Type.LESS_THAN)
    private Integer maxQuantity;
    @Query(propName = "points", type = Query.Type.GREATER_THAN)
    private Integer minPoints;
    @Query(propName = "points", type = Query.Type.LESS_THAN)
    private Integer maxPoints;

}
