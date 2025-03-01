package com.piotrpabich.talktactics.user.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeleteFriendRequest(
        @NotNull
        UUID userUuid,
        @NotNull
        UUID friendUuid
) { }
