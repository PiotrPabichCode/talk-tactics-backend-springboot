package com.piotrpabich.talktactics.dto.course;

import com.piotrpabich.talktactics.common.Query;
import com.piotrpabich.talktactics.dto.user_course.UserCourseQueryCriteria;
import com.piotrpabich.talktactics.entity.CourseLevel;
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
    @Query(propName = "id", type = Query.Type.NOT_IN)
    private Set<Long> notInCourseIds = new HashSet<>();

    public static CourseQueryCriteria fromUserCourseQueryCriteria(UserCourseQueryCriteria criteria, Set<Long> excludeCourseIds) {
        CourseQueryCriteria courseQueryCriteria = new CourseQueryCriteria();
        courseQueryCriteria.setTitle(criteria.getCourseTitle());
        courseQueryCriteria.setDescription(criteria.getCourseDescription());
        courseQueryCriteria.setLevels(criteria.getCourseLevels());
        courseQueryCriteria.setMinQuantity(criteria.getMinQuantity());
        courseQueryCriteria.setMaxQuantity(criteria.getMaxQuantity());
        courseQueryCriteria.setMinPoints(criteria.getMinPoints());
        courseQueryCriteria.setMaxPoints(criteria.getMaxPoints());
        courseQueryCriteria.setNotInCourseIds(excludeCourseIds);
        return courseQueryCriteria;
    }
}
