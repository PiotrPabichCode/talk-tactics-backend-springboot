package com.piotrpabich.talktactics.course.word.dto;

import com.piotrpabich.talktactics.common.Query;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseWordQueryCriteria {
    @NotNull(message = "courseUuid property cannot be null")
    private UUID courseUuid;
    @Query(propName = "word", type = Query.Type.INNER_LIKE)
    private String word;
    @Query(propName = "partOfSpeech", type = Query.Type.INNER_LIKE)
    private String partOfSpeech;
    @Query(propName = "phonetic", type = Query.Type.NOT_NULL)
    private String phonetic;
    @Query(blurry = "word,partOfSpeech,phonetic", propName = "search")
    private String search;
}
