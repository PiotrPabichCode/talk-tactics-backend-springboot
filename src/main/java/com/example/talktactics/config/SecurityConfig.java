package com.example.talktactics.config;

import com.example.talktactics.config.security.JwtAuthenticationFilter;
import com.example.talktactics.util.Constants;
import com.example.talktactics.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

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
        new AntPathRequestMatcher("/api/v1/courses/all"),
        new AntPathRequestMatcher("/api/v1/courses/navbar"),
        new AntPathRequestMatcher("/api/v1/courses/all/preview"),
        new AntPathRequestMatcher("/api/v1/auth/**"),
        new AntPathRequestMatcher("/api/v1/courses/id/{id}", HttpMethod.GET.name()),
        new AntPathRequestMatcher("/api/v1/courses/level/{level}"),
        new AntPathRequestMatcher("/api/v1/users/create"),
        new AntPathRequestMatcher("/api/v1/users/profiles")
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Creating securityFilterChain for App Server...");
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(PERMITTED_ENDPOINTS).permitAll()
                .requestMatchers(WHITELIST_URLS).permitAll()
                .requestMatchers("/api/v1/courses/create").hasAnyAuthority(Constants.ADMIN)
                .requestMatchers(HttpMethod.PUT, "/api/v1/courses/id/{id}").hasAnyAuthority(Constants.ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/courses/id/{id}").hasAnyAuthority(Constants.ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/v1/course-items/id/{id}").permitAll()
                .requestMatchers("/api/v1/course-items/preview/courses/id/{id}").permitAll()
                .requestMatchers("/api/v1/course-items/all").hasAnyAuthority(Constants.ADMIN)
                .requestMatchers(HttpMethod.DELETE,"/api/v1/course-items/id/{id}").hasAnyAuthority(Constants.ADMIN)
                .requestMatchers("/api/v1/users/all").hasAnyAuthority(Constants.ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/v1/users/id/{id}").hasAnyAuthority(Constants.USER, Constants.ADMIN)
                .requestMatchers(HttpMethod.PATCH, "/api/v1/users/id/{id}").hasAnyAuthority(Constants.USER, Constants.ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/id/{id}").hasAnyAuthority(Constants.ADMIN)
                .requestMatchers("/api/v1/users/password").hasAnyAuthority(Constants.USER, Constants.ADMIN)
                .requestMatchers("/api/v1/users/username/{username}").hasAnyAuthority(Constants.USER, Constants.ADMIN)
                .requestMatchers("/api/v1/user-courses/all").hasAnyAuthority(Constants.ADMIN)
                .requestMatchers("/api/v1/user-courses/preview/user-id/{id}").hasAnyAuthority(Constants.USER, Constants.ADMIN)
                .requestMatchers("/api/v1/user-courses/id/{id}").hasAnyAuthority(Constants.USER, Constants.ADMIN)
                .requestMatchers("/api/v1/user-courses/user-id/{id}").hasAnyAuthority(Constants.USER, Constants.ADMIN)
                .requestMatchers("/api/v1/user-courses").hasAnyAuthority(Constants.USER, Constants.ADMIN)
                .requestMatchers("/api/v1/user-course-items/learn/id/{id}").hasAnyAuthority(Constants.USER, Constants.ADMIN)
                .requestMatchers("/api/v1/user-course-items/all/preview").hasAnyAuthority(Constants.USER, Constants.ADMIN)
                .requestMatchers("/api/v1/user-course-items/id/{id}").hasAnyAuthority(Constants.USER, Constants.ADMIN)
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
