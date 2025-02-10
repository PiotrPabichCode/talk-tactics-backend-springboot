package com.piotrpabich.talktactics.auth;

import com.piotrpabich.talktactics.auth.dto.AuthenticationRequest;
import com.piotrpabich.talktactics.auth.dto.AuthenticationResponse;
import com.piotrpabich.talktactics.auth.dto.RefreshTokenRequest;
import com.piotrpabich.talktactics.auth.dto.RegisterRequest;
import com.piotrpabich.talktactics.auth.token.TokenService;
import com.piotrpabich.talktactics.exception.BadRequestException;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.user.entity.Role;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user.UserRepository;
import com.piotrpabich.talktactics.util.EmailValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(final RegisterRequest request) {
        validateNewUser(request);
        final var user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(Role.USER)
                .build();
        repository.save(user);
        final var jwtToken = tokenService.generateToken(user);
        final var jwtRefreshToken = tokenService.generateRefreshToken(user);
        return new AuthenticationResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                jwtToken,
                jwtRefreshToken
        );
    }
    @Override
    public AuthenticationResponse authenticate(final AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        var user = repository.findByUsername(request.username())
                .orElseThrow();
        var jwtToken = tokenService.generateToken(user);
        var jwtRefreshToken = tokenService.generateRefreshToken(user);
        return new AuthenticationResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                jwtToken,
                jwtRefreshToken
        );
    }
    @Override
    public AuthenticationResponse reauthenticate(RefreshTokenRequest request) {
        var user = repository.findByUsername(request.username())
                .orElseThrow();
        var jwtToken = tokenService.generateToken(user);
        var jwtRefreshToken = tokenService.generateRefreshToken(user);
        return new AuthenticationResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                jwtToken,
                jwtRefreshToken
        );
    }

    @Override
    public User getUserFromRequest(final HttpServletRequest request) {
        var token = getTokenFromRequest(request);
        var username = tokenService.extractUsername(token);
        return repository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "username", username));
    }

    private String getTokenFromRequest(final HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }

    private boolean isEmpty(final String value) {
        return value == null || value.isEmpty();
    }

    private void validateNewUser(final RegisterRequest request) {
        if(isEmpty(request.username()) || isEmpty(request.password()) || isEmpty(request.repeatPassword())
                || isEmpty(request.email()) || isEmpty(request.firstName()) || isEmpty(request.lastName())) {
            throw new BadRequestException("Fields cannot be empty");
        }
        if(repository.existsByUsername(request.username())) {
            throw new BadRequestException("Username exists");
        }
        if(repository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already used");
        }
        if(!EmailValidator.isValidEmail(request.email())) {
            throw new BadRequestException("Email is not valid");
        }
        if(!request.password().equals(request.repeatPassword())) {
            throw new BadRequestException("Passwords must match each other");
        }
    }
}
