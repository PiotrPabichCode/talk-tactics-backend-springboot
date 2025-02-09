package com.piotrpabich.talktactics.user.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdatePasswordRequest(
        @JsonProperty("id")
        Long id,
        @JsonProperty("old_password")
        String oldPassword,
        @JsonProperty("new_password")
        String newPassword,
        @JsonProperty("repeat_new_password")
        String repeatNewPassword
) { }
