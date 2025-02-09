package com.piotrpabich.talktactics.course_item.dto;

import com.piotrpabich.talktactics.course.entity.CourseLevel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.piotrpabich.talktactics.course_item.entity.CourseItem;

public record CourseItemDto(
        Long id,
        String word,
        @JsonProperty("part_of_speech")
        String partOfSpeech,
        String phonetic,
        String audio,
        CourseLevel level
) {

    public static CourseItemDto toDto(final CourseItem courseItem) {
        return new CourseItemDto(
                courseItem.getId(),
                courseItem.getWord(),
                courseItem.getPartOfSpeech(),
                courseItem.getPhonetic(),
                courseItem.getAudio(),
                courseItem.getLevel()
        );
    }
}
