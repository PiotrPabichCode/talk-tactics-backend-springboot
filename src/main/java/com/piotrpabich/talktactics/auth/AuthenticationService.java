package com.piotrpabich.talktactics.auth;

import com.piotrpabich.talktactics.auth.dto.req.AuthenticationRequest;
import com.piotrpabich.talktactics.auth.dto.res.AuthenticationResponse;
import com.piotrpabich.talktactics.auth.dto.req.RefreshTokenRequest;
import com.piotrpabich.talktactics.auth.dto.req.RegisterRequest;
import com.piotrpabich.talktactics.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse reauthenticate(RefreshTokenRequest request);
    User getUserFromRequest(final HttpServletRequest request);
}
