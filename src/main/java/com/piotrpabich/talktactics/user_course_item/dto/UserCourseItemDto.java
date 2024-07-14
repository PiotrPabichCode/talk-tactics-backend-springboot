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

    public static UserCourseItemDto toDto(UserCourseItem userCourseItem) {
        return new UserCourseItemDto(
                userCourseItem.getId(),
                userCourseItem.getCourseItem().getWord(),
                userCourseItem.getCourseItem().getPartOfSpeech(),
                userCourseItem.getCourseItem().getPhonetic(),
                userCourseItem.getCourseItem().getAudio(),
                userCourseItem.getCourseItem().getLevel(),
                userCourseItem.isLearned()
        );
    }
}
