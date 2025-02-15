package com.piotrpabich.talktactics.user.dto;

import jakarta.validation.constraints.NotNull;

public record FriendInvitationRequest(
        @NotNull
        Long senderId,
        @NotNull
        Long receiverId,
        @NotNull
        FriendInvitationAction action
) {
}
