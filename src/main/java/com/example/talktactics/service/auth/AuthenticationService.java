package com.example.talktactics.service.auth;

import com.example.talktactics.dto.auth.req.AuthenticationRequest;
import com.example.talktactics.dto.auth.res.AuthenticationResponse;
import com.example.talktactics.dto.auth.req.RefreshTokenRequest;
import com.example.talktactics.dto.auth.req.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse reauthenticate(RefreshTokenRequest request);
}
