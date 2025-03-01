package com.piotrpabich.talktactics.course_item.dto;

import com.piotrpabich.talktactics.course.entity.CourseLevel;
import com.piotrpabich.talktactics.course_item.entity.CourseItem;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseItemPreview(
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

    public static CourseItemPreview of(final CourseItem courseItem) {
        return new CourseItemPreview(
                courseItem.getUuid(),
                courseItem.getWord(),
                courseItem.getPartOfSpeech(),
                courseItem.getPhonetic(),
                courseItem.getAudio(),
                courseItem.getCourse().getLevel()
        );
    }
}
