package com.example.talktactics.dto.user_course;

import com.example.talktactics.common.Query;
import com.example.talktactics.entity.CourseLevel;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserCourseQueryCriteria {
    private Boolean tableRow;
    private Boolean fetchCourses;
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
    private Set<Long> userIds;
    @Query(propName = "id", joinName = "course", type = Query.Type.IN)
    private Set<Long> courseIds;
    @Query(propName = "title", joinName = "course", type = Query.Type.INNER_LIKE)
    private String courseTitle;
    @Query(propName = "description", joinName = "course", type = Query.Type.INNER_LIKE)
    private String courseDescription;
    @Query(propName = "level", joinName = "course", type = Query.Type.IN)
    private Set<CourseLevel> courseLevels;
    @Query(propName = "quantity", joinName = "course", type = Query.Type.GREATER_THAN)
    private Integer minQuantity;
    @Query(propName = "quantity", joinName = "course", type = Query.Type.LESS_THAN)
    private Integer maxQuantity;
}
