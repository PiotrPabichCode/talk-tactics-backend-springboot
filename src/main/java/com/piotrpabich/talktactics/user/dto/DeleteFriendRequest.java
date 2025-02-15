package com.piotrpabich.talktactics.user.dto;

import jakarta.validation.constraints.NotNull;

public record DeleteFriendRequest(
        @NotNull
        Long userId,
        @NotNull
        Long friendId
) { }
