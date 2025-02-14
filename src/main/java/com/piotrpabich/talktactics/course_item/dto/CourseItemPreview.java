package com.piotrpabich.talktactics.course_item.dto;

import com.piotrpabich.talktactics.course.entity.CourseLevel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.piotrpabich.talktactics.course_item.entity.CourseItem;

public record CourseItemPreview(
        Long id,
        String word,
        @JsonProperty("part_of_speech")
        String partOfSpeech,
        String phonetic,
        String audio,
        CourseLevel level
) {

    public static CourseItemPreview toDto(final CourseItem courseItem) {
        return new CourseItemPreview(
                courseItem.getId(),
                courseItem.getWord(),
                courseItem.getPartOfSpeech(),
                courseItem.getPhonetic(),
                courseItem.getAudio(),
                courseItem.getLevel()
        );
    }
}
