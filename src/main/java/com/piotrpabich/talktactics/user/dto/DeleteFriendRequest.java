package com.piotrpabich.talktactics.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeleteFriendRequest(
        @JsonProperty("user_id")
        Long userId,
        @JsonProperty("friend_id")
        Long friendId
) { }
