package com.piotrpabich.talktactics.auth;

import com.piotrpabich.talktactics.auth.dto.req.AuthenticationRequest;
import com.piotrpabich.talktactics.auth.dto.req.RefreshTokenRequest;
import com.piotrpabich.talktactics.auth.dto.req.RegisterRequest;
import com.piotrpabich.talktactics.auth.dto.res.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.piotrpabich.talktactics.common.AppConst.API_V1;
import static com.piotrpabich.talktactics.common.AppConst.AUTH_PATH;

@RestController
@RequestMapping(API_V1 + AUTH_PATH)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(service.reauthenticate(request));
    }
}
