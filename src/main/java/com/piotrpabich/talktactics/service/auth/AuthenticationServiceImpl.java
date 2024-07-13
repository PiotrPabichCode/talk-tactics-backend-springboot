package com.piotrpabich.talktactics.service.auth;

import com.piotrpabich.talktactics.dto.auth.req.AuthenticationRequest;
import com.piotrpabich.talktactics.dto.auth.res.AuthenticationResponse;
import com.piotrpabich.talktactics.dto.auth.req.RefreshTokenRequest;
import com.piotrpabich.talktactics.dto.auth.req.RegisterRequest;
import com.piotrpabich.talktactics.exception.BadRequestException;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.service.jwt.JwtService;
import com.piotrpabich.talktactics.entity.Role;
import com.piotrpabich.talktactics.entity.User;
import com.piotrpabich.talktactics.repository.UserRepository;
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
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

//  PUBLIC
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        validateNewUser(request);
        var user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        return new AuthenticationResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                jwtToken,
                jwtRefreshToken
        );
    }
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        var user = repository.findByUsername(request.username())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
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
        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
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
        var username = jwtService.extractUsername(token);
        return repository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "username", username));
    }

//  PRIVATE

    private String getTokenFromRequest(final HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
    private boolean isEmpty(String value) {
    return value == null || value.isEmpty();
}
    private void validateNewUser(RegisterRequest request) {
        if( isEmpty(request.username()) || isEmpty(request.password()) || isEmpty(request.repeatPassword())
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
