package com.piotrpabich.talktactics.user_course_item.dto;

import com.piotrpabich.talktactics.course.entity.CourseLevel;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;

public record UserCourseItemDto(
        Long id,
        String word,
        String partOfSpeech,
        String phonetic,
        String audio,
        CourseLevel level,
        Boolean isLearned
) {

    public static UserCourseItemDto toDto(final UserCourseItem userCourseItem) {
        final var courseItem = userCourseItem.getCourseItem();
        return new UserCourseItemDto(
                userCourseItem.getId(),
                courseItem.getWord(),
                courseItem.getPartOfSpeech(),
                courseItem.getPhonetic(),
                courseItem.getAudio(),
                courseItem.getCourse().getLevel(),
                userCourseItem.isLearned()
        );
    }
}
