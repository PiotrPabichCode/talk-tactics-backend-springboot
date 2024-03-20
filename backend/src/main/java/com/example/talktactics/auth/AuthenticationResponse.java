package com.example.talktactics.auth;

import com.example.talktactics.entity.Role;
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
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String token;
    private String refreshToken;
}
