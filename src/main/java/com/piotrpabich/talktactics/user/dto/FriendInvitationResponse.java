package com.piotrpabich.talktactics.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FriendInvitationResponse(
        @NotNull
        UUID senderUuid,
        @NotNull
        UUID receiverUuid,
        UserProfilePreviewDto sender,
        UserProfilePreviewDto receiver
) {
}
