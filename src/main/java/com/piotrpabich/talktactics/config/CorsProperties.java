package com.piotrpabich.talktactics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "web.cors")
public record CorsProperties(
        List<String> allowedOrigins,
        List<String> allowedMethods,
        Long maxAge,
        List<String> allowedHeaders,
        Boolean allowCredentials
) {
}
