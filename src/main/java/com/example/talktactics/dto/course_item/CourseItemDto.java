package com.example.talktactics.dto.course_item;

import com.example.talktactics.entity.CourseItem;
import com.example.talktactics.entity.CourseLevel;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CourseItemDto(
        Long id,
        String word,
        @JsonProperty("part_of_speech")
        String partOfSpeech,
        String phonetic,
        String audio,
        CourseLevel level
) {

    public static CourseItemDto toDto(CourseItem courseItem) {
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
