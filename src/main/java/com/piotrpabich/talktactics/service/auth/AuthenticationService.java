package com.piotrpabich.talktactics.service.auth;

import com.piotrpabich.talktactics.dto.auth.req.AuthenticationRequest;
import com.piotrpabich.talktactics.dto.auth.res.AuthenticationResponse;
import com.piotrpabich.talktactics.dto.auth.req.RefreshTokenRequest;
import com.piotrpabich.talktactics.dto.auth.req.RegisterRequest;
import com.piotrpabich.talktactics.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse reauthenticate(RefreshTokenRequest request);
    User getUserFromRequest(final HttpServletRequest request);
}
