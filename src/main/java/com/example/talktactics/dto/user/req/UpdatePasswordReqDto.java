package com.example.talktactics.dto.user.req;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePasswordReqDto {
    Long id;
    @JsonProperty("old_password")
    String oldPassword;
    @JsonProperty("new_password")
    String newPassword;
    @JsonProperty("repeat_new_password")
    String repeatNewPassword;
}
