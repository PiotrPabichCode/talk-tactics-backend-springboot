package com.piotrpabich.talktactics.course.participant.dto;

import com.piotrpabich.talktactics.course.dto.CourseDto;
import com.piotrpabich.talktactics.course.participant.entity.CourseParticipant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseParticipantDto(
        @NotNull
        UUID uuid,
        @Min(0)
        Double progress,
        @NotNull
        Boolean completed,
        @Min(0)
        Integer points,
        @NotNull
        CourseDto course
) {
    public static CourseParticipantDto of(final CourseParticipant courseParticipant) {
        return new CourseParticipantDto(
                courseParticipant.getUuid(),
                courseParticipant.getProgress(),
                courseParticipant.getCompleted(),
                courseParticipant.getPoints(),
                CourseDto.of(courseParticipant.getCourse())
        );
    }
}
