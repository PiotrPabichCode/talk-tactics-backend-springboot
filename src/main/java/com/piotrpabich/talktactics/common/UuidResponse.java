package com.piotrpabich.talktactics.common;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UuidResponse(@NotNull UUID uuid) {

    public static UuidResponse of(final UUID uuid) {
        return new UuidResponse(uuid);
    }
}
