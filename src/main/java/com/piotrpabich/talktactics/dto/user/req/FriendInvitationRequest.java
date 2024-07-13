package com.piotrpabich.talktactics.dto.user.req;

import com.piotrpabich.talktactics.dto.user.FriendInvitationAction;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FriendInvitationRequest(
        @JsonProperty("sender_id")
        Long senderId,
        @JsonProperty("receiver_id")
        Long receiverId,
        FriendInvitationAction action
) {
}
