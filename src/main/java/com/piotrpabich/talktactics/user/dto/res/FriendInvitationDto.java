package com.piotrpabich.talktactics.user.dto.res;

import com.piotrpabich.talktactics.user.dto.UserProfilePreviewDto;
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
