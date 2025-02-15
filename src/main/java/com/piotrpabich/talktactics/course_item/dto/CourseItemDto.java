package com.piotrpabich.talktactics.course_item.dto;

import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course_item.entity.CourseItem;
import com.piotrpabich.talktactics.course_item.entity.Meaning;

import java.util.List;

public record CourseItemDto(
        Long id,
        String word,
        String partOfSpeech,
        String phonetic,
        String audio,
        List<Meaning> meanings,
        CourseDto course
) {

    public static CourseItemDto toDto(final CourseItem courseItem) {
        return new CourseItemDto(
                courseItem.getId(),
                courseItem.getWord(),
                courseItem.getPartOfSpeech(),
                courseItem.getPhonetic(),
                courseItem.getAudio(),
                courseItem.getMeanings(),
                CourseDto.from(courseItem.getCourse())
        );
    }
}
