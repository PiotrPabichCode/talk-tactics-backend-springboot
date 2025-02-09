package com.piotrpabich.talktactics.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FriendInvitationResponse(
        @JsonProperty("sender_id")
        Long senderId,
        @JsonProperty("receiver_id")
        Long receiverId,
        UserProfilePreviewDto sender,
        UserProfilePreviewDto receiver
) {
}
