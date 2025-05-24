package com.piotrpabich.talktactics.config;

import com.piotrpabich.talktactics.auth.AuthConstants;
import com.piotrpabich.talktactics.config.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Log4j2
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final AntPathRequestMatcher[] WHITELIST_URLS = {
            new AntPathRequestMatcher("/v2/api-docs"),
            new AntPathRequestMatcher("/v3/api-docs"),
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/swagger-resources"),
            new AntPathRequestMatcher("/swagger-resources/**"),
            new AntPathRequestMatcher("/configuration/ui"),
            new AntPathRequestMatcher("/configuration/security"),
            new AntPathRequestMatcher("/swagger-ui.html"),
            new AntPathRequestMatcher("/webjars/**"),
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/error"),
            new AntPathRequestMatcher("/api/v1/auth/**"),
            new AntPathRequestMatcher("/api/v1/courses/navbar"),
            new AntPathRequestMatcher("/api/v1/course-participants"),
            new AntPathRequestMatcher("/api/v1/course-participants/{courseParticipantUuid}"),
            new AntPathRequestMatcher("/api/v1/users/profiles"),
            new AntPathRequestMatcher("/api/v1/users/profiles/{userUuid}"),
            new AntPathRequestMatcher("/api/v1/courses", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/api/v1/course-words", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/api/v1/course-words/{courseUuid}", HttpMethod.GET.name())
    };
    private static final AntPathRequestMatcher[] ADMIN_URLS = {
            new AntPathRequestMatcher("/api/v1/courses", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/api/v1/courses", HttpMethod.PATCH.name()),
            new AntPathRequestMatcher("/api/v1/courses", HttpMethod.DELETE.name()),
            new AntPathRequestMatcher("/api/v1/course-words", HttpMethod.DELETE.name()),
            new AntPathRequestMatcher("/api/v1/users")
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Creating securityFilterChain for App Server...");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITELIST_URLS).permitAll()
                        .requestMatchers(ADMIN_URLS).hasAnyAuthority(AuthConstants.ADMIN)
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
