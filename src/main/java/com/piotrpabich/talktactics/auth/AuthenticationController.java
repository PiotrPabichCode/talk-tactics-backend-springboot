package com.piotrpabich.talktactics.auth;

import com.piotrpabich.talktactics.auth.dto.AuthenticationRequest;
import com.piotrpabich.talktactics.auth.dto.RefreshTokenRequest;
import com.piotrpabich.talktactics.auth.dto.RegisterRequest;
import com.piotrpabich.talktactics.auth.dto.AuthenticationResponse;
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
            @RequestBody final RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody final AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestBody final RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(service.reauthenticate(request));
    }
}
