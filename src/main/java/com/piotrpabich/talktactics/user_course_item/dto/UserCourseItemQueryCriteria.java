package com.piotrpabich.talktactics.user_course_item.dto;

import com.piotrpabich.talktactics.common.Query;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UserCourseItemQueryCriteria {
    @NotNull(message = "courseUuid property cannot be null")
    private UUID courseUuid;
    @NotNull(message = "userUuid property cannot be null")
    private UUID userUuid;
    @Query
    private Boolean isLearned;
    @Query(propName = "word", joinName = "courseItem" , type = Query.Type.INNER_LIKE)
    private String word;
    @Query(propName = "partOfSpeech", joinName = "courseItem" ,type = Query.Type.INNER_LIKE)
    private String partOfSpeech;
    @Query(propName = "phonetic", joinName = "courseItem" ,type = Query.Type.NOT_NULL)
    private String phonetic;
}
