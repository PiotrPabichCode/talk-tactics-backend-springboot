package com.example.talktactics.auth;

import com.example.talktactics.config.JwtService;
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
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        validateNewUser(request);
        var user = User.builder()
                .login(request.getLogin())
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

    private boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }
    private void validateNewUser(RegisterRequest request) {
        if(isEmpty(request.getLogin()) || isEmpty(request.getPassword()) || isEmpty(request.getRepeatPassword())
                || isEmpty(request.getEmail()) || isEmpty(request.getFirstName()) || isEmpty(request.getLastName())) {
            throw new RuntimeException("Fields cannot be empty");
        }
        if(repository.existsByLogin(request.getLogin())) {
            throw new RuntimeException("Login exists");
        }
        if(repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already used");
        }
        if(!request.getPassword().equals(request.getRepeatPassword())) {
            throw new RuntimeException("Passwords must match each other");
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
        var user = repository.findByLogin(request.getLogin())
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

    public AuthenticationResponse reauthenticate(RefreshTokenRequest request) {
        var user = repository.findByLogin(request.getLogin())
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
}
