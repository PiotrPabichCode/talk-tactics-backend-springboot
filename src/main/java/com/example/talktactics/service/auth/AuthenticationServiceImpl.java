package com.example.talktactics.service.auth;

import com.example.talktactics.dto.auth.req.AuthenticationRequest;
import com.example.talktactics.dto.auth.res.AuthenticationResponse;
import com.example.talktactics.dto.auth.req.RefreshTokenRequest;
import com.example.talktactics.dto.auth.req.RegisterRequest;
import com.example.talktactics.service.jwt.JwtService;
import com.example.talktactics.entity.Role;
import com.example.talktactics.entity.User;
import com.example.talktactics.repository.UserRepository;
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
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }
    @Override
    public AuthenticationResponse reauthenticate(RefreshTokenRequest request) {
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

//  PRIVATE
    private boolean isEmpty(String value) {
    return value == null || value.isEmpty();
}
    private void validateNewUser(RegisterRequest request) {
        if(isEmpty(request.getUsername()) || isEmpty(request.getPassword()) || isEmpty(request.getRepeatPassword())
                || isEmpty(request.getEmail()) || isEmpty(request.getFirstName()) || isEmpty(request.getLastName())) {
            throw new RuntimeException("Fields cannot be empty");
        }
        if(repository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username exists");
        }
        if(repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already used");
        }
        if(!request.getPassword().equals(request.getRepeatPassword())) {
            throw new RuntimeException("Passwords must match each other");
        }
    }
}
