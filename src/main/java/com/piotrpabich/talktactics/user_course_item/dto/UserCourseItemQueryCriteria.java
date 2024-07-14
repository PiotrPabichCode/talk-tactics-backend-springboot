package com.piotrpabich.talktactics.user_course_item.dto;

import com.piotrpabich.talktactics.common.Query;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCourseItemQueryCriteria {
    @NotNull(message = "courseId property cannot be null")
    Long courseId;
    @NotNull(message = "userId property cannot be null")
    Long userId;
    @Query
    private Boolean isLearned;
    @Query(propName = "word", joinName = "courseItem" , type = Query.Type.INNER_LIKE)
    private String word;
    @Query(propName = "partOfSpeech", joinName = "courseItem" ,type = Query.Type.INNER_LIKE)
    private String partOfSpeech;
    @Query(propName = "phonetic", joinName = "courseItem" ,type = Query.Type.NOT_NULL)
    private String phonetic;
}
