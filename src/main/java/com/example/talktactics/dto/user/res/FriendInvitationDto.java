package com.example.talktactics.dto.user.res;

import com.example.talktactics.dto.user.UserProfilePreviewDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FriendInvitationDto(
        @JsonProperty("sender_id")
        Long senderId,
        @JsonProperty("receiver_id")
        Long receiverId,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        UserProfilePreviewDto sender,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        UserProfilePreviewDto receiver
) {
}
