package com.piotrpabich.talktactics.user.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeleteFriendDto(
        @JsonProperty("user_id")
        Long userId,
        @JsonProperty("friend_id")
        Long friendId
) { }
