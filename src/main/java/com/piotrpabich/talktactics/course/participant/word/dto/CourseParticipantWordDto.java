package com.piotrpabich.talktactics.course.participant.word.dto;

import com.piotrpabich.talktactics.course.entity.CourseLevel;
import com.piotrpabich.talktactics.course.participant.word.entity.CourseParticipantWord;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseParticipantWordDto(
        @NotNull
        UUID uuid,
        @NotBlank
        String word,
        @NotBlank
        String partOfSpeech,
        @NotBlank
        String phonetic,
        @NotBlank
        String audio,
        @NotNull
        CourseLevel level,
        @NotNull
        Boolean isLearned
) {

    public static CourseParticipantWordDto of(final CourseParticipantWord courseParticipantWord) {
        final var courseWord = courseParticipantWord.getCourseWord();
        return new CourseParticipantWordDto(
                courseParticipantWord.getUuid(),
                courseWord.getWord(),
                courseWord.getPartOfSpeech(),
                courseWord.getPhonetic(),
                courseWord.getAudio(),
                courseWord.getCourse().getLevel(),
                courseParticipantWord.isLearned()
        );
    }
}
