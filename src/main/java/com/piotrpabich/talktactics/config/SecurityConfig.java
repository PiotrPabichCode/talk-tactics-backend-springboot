package com.piotrpabich.talktactics.config;

import com.piotrpabich.talktactics.auth.AuthConstants;
import com.piotrpabich.talktactics.config.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private static final String[] PERMITTED_ENDPOINTS = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-ui/**",
    };

    public static final RequestMatcher[] WHITELIST_URLS = {
        new AntPathRequestMatcher("/error"),
        new AntPathRequestMatcher("/api/v1/auth/**"),
        new AntPathRequestMatcher("/api/v1/courses/navbar"),
        new AntPathRequestMatcher("/api/v1/users/profiles"),
        new AntPathRequestMatcher("/api/v1/users/profiles/{userId}")
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Creating securityFilterChain for App Server...");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PERMITTED_ENDPOINTS).permitAll()
                        .requestMatchers(WHITELIST_URLS).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/courses").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/courses").hasAnyAuthority(AuthConstants.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/courses").hasAnyAuthority(AuthConstants.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/courses").hasAnyAuthority(AuthConstants.ADMIN)
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/course-items").hasAnyAuthority(AuthConstants.ADMIN)
                        .requestMatchers(HttpMethod.GET,"/api/v1/course-items").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/course-items/{id}").permitAll()
                        .requestMatchers("/api/v1/users").hasAnyAuthority(AuthConstants.ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/id/{id}").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .requestMatchers( "/api/v1/users/id/{id}/friends").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .requestMatchers( "/api/v1/users/friend-invitation").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .requestMatchers( "/api/v1/users/delete-friend").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .requestMatchers( "/api/v1/users/id/{id}/received-friend-invitations").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .requestMatchers( "/api/v1/users/id/{id}/sent-friend-invitations").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/id/{id}").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/id/{id}").hasAnyAuthority(AuthConstants.ADMIN)
                        .requestMatchers("/api/v1/users/password").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .requestMatchers("/api/v1/users/username/{username}").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .requestMatchers("/api/v1/user-courses").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .requestMatchers("/api/v1/user-courses/id/{id}").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .requestMatchers("/api/v1/user-courses").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .requestMatchers("/api/v1/user-course-items").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .requestMatchers("/api/v1/user-course-items/learn/id/{id}").hasAnyAuthority(AuthConstants.USER, AuthConstants.ADMIN)
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
