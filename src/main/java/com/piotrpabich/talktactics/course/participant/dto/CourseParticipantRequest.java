package com.piotrpabich.talktactics.course.participant.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseParticipantRequest(
        @NotNull
        UUID courseUuid,
        @NotNull
        UUID userUuid
) {
}
