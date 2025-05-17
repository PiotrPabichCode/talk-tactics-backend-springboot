package com.piotrpabich.talktactics.auth;

import com.piotrpabich.talktactics.auth.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.piotrpabich.talktactics.common.AppConst.API_V1;
import static com.piotrpabich.talktactics.common.AppConst.AUTH_PATH;

@RestController
@RequestMapping(API_V1 + AUTH_PATH)
@RequiredArgsConstructor
@Tag(name = "AuthenticationController")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid final RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid final AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestBody @Valid final RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(authenticationService.reauthenticate(request));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @RequestBody @Valid final UpdatePasswordRequest updatePasswordRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        authenticationService.updatePassword(updatePasswordRequest, requester);
        return ResponseEntity.noContent().build();
    }
}
