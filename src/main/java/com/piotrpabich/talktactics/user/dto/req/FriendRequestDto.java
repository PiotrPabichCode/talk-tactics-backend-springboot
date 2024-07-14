package com.piotrpabich.talktactics.user.dto.req;

import com.piotrpabich.talktactics.user.dto.UserProfilePreviewDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FriendRequestDto(
        @JsonProperty("sender_id")
        Long senderId,
        @JsonProperty("receiver_id")
        Long receiverId,
        @JsonInclude(Include.NON_NULL)
        UserProfilePreviewDto sender,
        @JsonInclude(Include.NON_NULL)
        UserProfilePreviewDto receiver
) {
}

