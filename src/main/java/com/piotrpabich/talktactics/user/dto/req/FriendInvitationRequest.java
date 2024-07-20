package com.piotrpabich.talktactics.user.dto.req;

import com.piotrpabich.talktactics.user.dto.FriendInvitationAction;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FriendInvitationRequest(
        @JsonProperty("sender_id")
        Long senderId,
        @JsonProperty("receiver_id")
        Long receiverId,
        FriendInvitationAction action
) {
}
