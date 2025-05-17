package com.piotrpabich.talktactics.course.participant.dto;

import com.piotrpabich.talktactics.common.Query;
import com.piotrpabich.talktactics.course.entity.CourseLevel;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class CourseParticipantQueryCriteria {
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
    @Query(propName = "uuid", joinName = "user", type = Query.Type.IN)
    private Set<UUID> userUuids;
    @Query(propName = "uuid", joinName = "course", type = Query.Type.IN)
    private Set<UUID> courseUuids;
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
