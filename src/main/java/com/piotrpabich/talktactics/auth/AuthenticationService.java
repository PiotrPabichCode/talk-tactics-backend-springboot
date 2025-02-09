package com.piotrpabich.talktactics.auth;

import com.piotrpabich.talktactics.auth.dto.AuthenticationRequest;
import com.piotrpabich.talktactics.auth.dto.AuthenticationResponse;
import com.piotrpabich.talktactics.auth.dto.RefreshTokenRequest;
import com.piotrpabich.talktactics.auth.dto.RegisterRequest;
import com.piotrpabich.talktactics.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse reauthenticate(RefreshTokenRequest request);
    User getUserFromRequest(final HttpServletRequest request);
}
