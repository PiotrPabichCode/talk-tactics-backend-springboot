package com.example.talktactics.dto.auth.res;

import com.example.talktactics.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private int id;
    private String username;
    private Role role;
    private String token;
    @JsonProperty("refresh_token")
    private String refreshToken;
}
