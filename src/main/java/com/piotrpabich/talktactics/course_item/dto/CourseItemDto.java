package com.piotrpabich.talktactics.course_item.dto;

import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course_item.entity.CourseItem;
import com.piotrpabich.talktactics.course_item.entity.Meaning;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CourseItemDto(
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
        List<Meaning> meanings,
        @NotNull
        CourseDto course
) {

    public static CourseItemDto of(final CourseItem courseItem) {
        return new CourseItemDto(
                courseItem.getUuid(),
                courseItem.getWord(),
                courseItem.getPartOfSpeech(),
                courseItem.getPhonetic(),
                courseItem.getAudio(),
                courseItem.getMeanings(),
                CourseDto.of(courseItem.getCourse())
        );
    }
}
