package com.piotrpabich.talktactics.course.participant.word.dto;

import com.piotrpabich.talktactics.common.Query;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseParticipantWordQueryCriteria {
    @NotNull(message = "courseUuid property cannot be null")
    private UUID courseUuid;
    @NotNull(message = "userUuid property cannot be null")
    private UUID userUuid;
    @Query
    private Boolean isLearned;
    @Query(propName = "word", joinName = "courseWord" , type = Query.Type.INNER_LIKE)
    private String word;
    @Query(propName = "partOfSpeech", joinName = "courseWord" ,type = Query.Type.INNER_LIKE)
    private String partOfSpeech;
    @Query(propName = "phonetic", joinName = "courseWord" ,type = Query.Type.NOT_NULL)
    private String phonetic;
}
