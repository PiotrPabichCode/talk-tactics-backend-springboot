package com.example.talktactics.auth;

import com.example.talktactics.models.Role;
import com.example.talktactics.models.User;
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
    private String refreshToken;
}
