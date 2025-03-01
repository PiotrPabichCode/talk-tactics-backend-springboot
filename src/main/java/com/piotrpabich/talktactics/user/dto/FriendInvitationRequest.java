package com.piotrpabich.talktactics.user.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FriendInvitationRequest(
        @NotNull
        UUID senderUuid,
        @NotNull
        UUID receiverUuid,
        @NotNull
        FriendInvitationAction action
) {
}
