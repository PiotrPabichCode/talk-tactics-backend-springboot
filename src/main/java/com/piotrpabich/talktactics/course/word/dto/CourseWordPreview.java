package com.piotrpabich.talktactics.course.word.dto;

import com.piotrpabich.talktactics.course.entity.CourseLevel;
import com.piotrpabich.talktactics.course.word.entity.CourseWord;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseWordPreview(
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
        CourseLevel level
) {

    public static CourseWordPreview of(final CourseWord courseWord) {
        return new CourseWordPreview(
                courseWord.getUuid(),
                courseWord.getWord(),
                courseWord.getPartOfSpeech(),
                courseWord.getPhonetic(),
                courseWord.getAudio(),
                courseWord.getCourse().getLevel()
        );
    }
}
