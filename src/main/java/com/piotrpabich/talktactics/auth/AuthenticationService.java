package com.piotrpabich.talktactics.auth;

import com.piotrpabich.talktactics.auth.dto.AuthenticationRequest;
import com.piotrpabich.talktactics.auth.dto.AuthenticationResponse;
import com.piotrpabich.talktactics.auth.dto.RefreshTokenRequest;
import com.piotrpabich.talktactics.auth.dto.RegisterRequest;
import com.piotrpabich.talktactics.auth.token.TokenService;
import com.piotrpabich.talktactics.exception.BadRequestException;
import com.piotrpabich.talktactics.auth.dto.UpdatePasswordRequest;
import com.piotrpabich.talktactics.exception.NotFoundException;
import com.piotrpabich.talktactics.user.entity.Role;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(final RegisterRequest request) {
        validateNewUser(request);
        final var user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setRole(Role.USER);
        userRepository.save(user);
        final var jwtToken = tokenService.generateToken(user);
        final var jwtRefreshToken = tokenService.generateRefreshToken(user);
        return AuthenticationResponse.of(user, jwtToken, jwtRefreshToken);
    }

    public AuthenticationResponse authenticate(final AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        final var user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new NotFoundException(String.format("User with username: %s was not found", request.username())));
        final var jwtToken = tokenService.generateToken(user);
        final var jwtRefreshToken = tokenService.generateRefreshToken(user);
        return AuthenticationResponse.of(user, jwtToken, jwtRefreshToken);
    }

    public AuthenticationResponse reauthenticate(RefreshTokenRequest request) {
        final var user = userRepository.findByUsername(request.username())
                .orElseThrow();
        final var jwtToken = tokenService.generateToken(user);
        final var jwtRefreshToken = tokenService.generateRefreshToken(user);
        return AuthenticationResponse.of(user, jwtToken, jwtRefreshToken);
    }

    public User getUserFromRequest(final HttpServletRequest request) {
        final var token = getTokenFromRequest(request);
        final var username = tokenService.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("User with username: %s was not found", username)));
    }

    public void updatePassword(
            final UpdatePasswordRequest updatePasswordRequest,
            final User requester
    ) {
        final var user = userRepository.findByUuid(updatePasswordRequest.userUuid())
                .orElseThrow(() -> new NotFoundException(String.format("User with uuid: %s was not found", updatePasswordRequest.userUuid())));
        validateIfUserHimselfOrAdmin(requester, user);
        validatePassword(user, updatePasswordRequest);
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.newPassword()));
        userRepository.save(user);
    }

    private void validatePassword(
            final User user,
            final UpdatePasswordRequest request
    ) {
        if (passwordEncoder.matches(user.getPassword(), request.oldPassword())) {
            throw new BadRequestException(AuthConstants.INVALID_PASSWORD_EXCEPTION);
        }
        if (!request.newPassword().equals(request.repeatNewPassword())) {
            throw new BadRequestException(AuthConstants.THE_SAME_PASSWORD_EXCEPTION);
        }
        if (passwordEncoder.matches(user.getPassword(), request.newPassword())) {
            throw new BadRequestException(AuthConstants.DUPLICATED_PASSWORD_EXCEPTION);
        }
    }

    private String getTokenFromRequest(final HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }

    private void validateNewUser(final RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already used");
        }
    }
}
