package com.example.talktactics.dto.user_course;

import com.example.talktactics.common.Query;
import com.example.talktactics.entity.CourseLevel;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserCourseQueryCriteria {
    @Query
    private Boolean completed;
    @Query(propName = "points", type = Query.Type.GREATER_THAN)
    private Integer minPoints;
    @Query(propName = "points", type = Query.Type.LESS_THAN)
    private Integer maxPoints;
    @Query(propName = "progress", type = Query.Type.GREATER_THAN)
    private Double minProgress;
    @Query(propName = "progress", type = Query.Type.LESS_THAN)
    private Double maxProgress;
    @Query(propName = "id", joinName = "user", type = Query.Type.IN)
    private Set<Long> userIds = new HashSet<>();
    @Query(propName = "id", joinName = "course", type = Query.Type.IN)
    private Set<Long> courseIds = new HashSet<>();
    @Query(propName = "title", joinName = "course", type = Query.Type.INNER_LIKE)
    private String courseTitle;
    @Query(propName = "description", joinName = "course", type = Query.Type.INNER_LIKE)
    private String courseDescription;
    @Query(propName = "level", joinName = "course", type = Query.Type.IN)
    private Set<CourseLevel> courseLevels = new HashSet<>();
    @Query(propName = "quantity", joinName = "course", type = Query.Type.GREATER_THAN)
    private Integer minQuantity;
    @Query(propName = "quantity", joinName = "course", type = Query.Type.LESS_THAN)
    private Integer maxQuantity;
}