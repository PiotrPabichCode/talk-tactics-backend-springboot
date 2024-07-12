package com.example.talktactics.dto.user_course_item;

import com.example.talktactics.entity.CourseLevel;
import com.example.talktactics.entity.UserCourseItem;

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
