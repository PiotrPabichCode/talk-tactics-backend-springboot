package com.piotrpabich.talktactics.user.dto.req;


import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdatePasswordReqDto(
        @JsonProperty("id")
        Long id,
        @JsonProperty("old_password")
        String oldPassword,
        @JsonProperty("new_password")
        String newPassword,
        @JsonProperty("repeat_new_password")
        String repeatNewPassword
) { }
