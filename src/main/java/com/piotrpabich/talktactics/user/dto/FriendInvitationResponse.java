package com.piotrpabich.talktactics.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FriendInvitationResponse(
        @NotNull
        Long senderId,
        @NotNull
        Long receiverId,
        UserProfilePreviewDto sender,
        UserProfilePreviewDto receiver
) {
}
