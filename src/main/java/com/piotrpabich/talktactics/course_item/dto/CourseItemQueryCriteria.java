package com.piotrpabich.talktactics.course_item.dto;

import com.piotrpabich.talktactics.common.Query;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseItemQueryCriteria {
    @NotNull(message = "courseId property cannot be null")
    private Long courseId;
    @Query(propName = "word", type = Query.Type.INNER_LIKE)
    private String word;
    @Query(propName = "partOfSpeech", type = Query.Type.INNER_LIKE)
    private String partOfSpeech;
    @Query(propName = "phonetic", type = Query.Type.NOT_NULL)
    private String phonetic;
    @Query(blurry = "word,partOfSpeech,phonetic", propName = "search")
    private String search;
}
