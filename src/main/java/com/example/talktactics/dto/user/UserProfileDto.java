package com.example.talktactics.dto.user;

import com.example.talktactics.dto.user_course.UserCourseDetailsDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserProfileDto extends UserProfilePreviewDto {
    List<UserCourseDetailsDto> courses;

    public static UserProfileDto toUserProfileDto(UserProfilePreviewDto userProfile, List<UserCourseDetailsDto> courses) {
        return UserProfileDto.builder()
                .id(userProfile.getId())
                .firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .totalPoints(userProfile.getTotalPoints())
                .bio(userProfile.getBio())
                .courses(courses)
                .build();
    }
}
