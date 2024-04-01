package com.example.talktactics.config;

import com.example.talktactics.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(PERMITTED_ENDPOINTS).permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/courses/id/{id}").permitAll()
                .requestMatchers("/api/v1/courses/all/preview").permitAll()
                .requestMatchers("/api/v1/courses/level/{level}").permitAll()
                .requestMatchers("/api/v1/courses/create").hasAnyAuthority(Constants.ADMIN)
                .requestMatchers(HttpMethod.PUT, "/api/v1/courses/id/{id}").hasAnyAuthority(Constants.ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/courses/id/{id}").hasAnyAuthority(Constants.ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/v1/course-items/id/{id}").permitAll()
                .requestMatchers("/api/v1/course-items/preview/courses/id/{id}").permitAll()
                .requestMatchers("/api/v1/course-items/all").hasAnyAuthority(Constants.ADMIN)
                .requestMatchers(HttpMethod.DELETE,"/api/v1/course-items/id/{id}").hasAnyAuthority(Constants.ADMIN)
                .requestMatchers("/api/v1/users/create").permitAll()
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
