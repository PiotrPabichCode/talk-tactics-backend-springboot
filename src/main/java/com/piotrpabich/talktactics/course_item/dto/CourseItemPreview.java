package com.piotrpabich.talktactics.course_item.dto;

import com.piotrpabich.talktactics.course.entity.CourseLevel;
import com.piotrpabich.talktactics.course_item.entity.CourseItem;
import jakarta.validation.constraints.NotNull;

public record CourseItemPreview(
        @NotNull
        Long id,
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

    public static CourseItemPreview toDto(final CourseItem courseItem) {
        return new CourseItemPreview(
                courseItem.getId(),
                courseItem.getWord(),
                courseItem.getPartOfSpeech(),
                courseItem.getPhonetic(),
                courseItem.getAudio(),
                courseItem.getCourse().getLevel()
        );
    }
}
