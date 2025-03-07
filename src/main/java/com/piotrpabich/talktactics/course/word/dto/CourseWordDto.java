package com.piotrpabich.talktactics.course.word.dto;

import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course.word.entity.CourseWord;
import com.piotrpabich.talktactics.course.word.entity.CourseWordDefinition;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CourseWordDto(
        @NotNull
        UUID uuid,
        @NotNull
        String word,
        @NotNull
        String partOfSpeech,
        @NotNull
        String phonetic,
        @NotNull
        String audio,
        @NotNull
        List<CourseWordDefinition> definitions,
        @NotNull
        CourseDto course
) {

    public static CourseWordDto of(final CourseWord courseWord) {
        return new CourseWordDto(
                courseWord.getUuid(),
                courseWord.getWord(),
                courseWord.getPartOfSpeech(),
                courseWord.getPhonetic(),
                courseWord.getAudio(),
                courseWord.getDefinitions(),
                CourseDto.of(courseWord.getCourse())
        );
    }
}
