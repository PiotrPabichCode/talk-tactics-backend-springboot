package com.piotrpabich.talktactics.user_course_item.dto;

import com.piotrpabich.talktactics.course.entity.CourseLevel;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserCourseItemDto(
        @NotNull
        UUID uuid,
        @NotBlank
        String word,
        @NotBlank
        String partOfSpeech,
        @NotBlank
        String phonetic,
        @NotBlank
        String audio,
        @NotNull
        CourseLevel level,
        @NotNull
        Boolean isLearned
) {

    public static UserCourseItemDto of(final UserCourseItem userCourseItem) {
        final var courseItem = userCourseItem.getCourseItem();
        return new UserCourseItemDto(
                userCourseItem.getUuid(),
                courseItem.getWord(),
                courseItem.getPartOfSpeech(),
                courseItem.getPhonetic(),
                courseItem.getAudio(),
                courseItem.getCourse().getLevel(),
                userCourseItem.isLearned()
        );
    }
}
