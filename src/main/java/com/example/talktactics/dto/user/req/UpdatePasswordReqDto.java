package com.example.talktactics.dto.user.req;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdatePasswordReqDto {
    Long id;
    @JsonProperty("old_password")
    String oldPassword;
    @JsonProperty("new_password")
    String newPassword;
    @JsonProperty("repeat_new_password")
    String repeatNewPassword;
}
