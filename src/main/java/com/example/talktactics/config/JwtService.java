package com.example.talktactics.config;

import com.example.talktactics.util.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final UserDetailsService userDetailsService;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    public Optional<UserDetails> validateToken(String token) {
        try {
            String username = extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(!username.equals(userDetails.getUsername()) || isTokenExpired(token)) {
                return Optional.empty();
            }
            return Optional.of(userDetails);
        } catch (UsernameNotFoundException e) {
            log.error(Constants.USERNAME_NOT_FOUND_EXCEPTION);
            return Optional.empty();
        } catch (ExpiredJwtException e) {
            log.error(Constants.JWT_EXPIRED_EXCEPTION);
            return Optional.empty();
        } catch (SignatureException e) {
            log.error(Constants.JWT_SIGNATURE_EXCEPTION);
            return Optional.empty();
        } catch (UnsupportedJwtException e) {
            log.error(Constants.JWT_UNSUPPORTED_EXCEPTION);
            return Optional.empty();
        } catch (MalformedJwtException e) {
            log.error(Constants.JWT_MALFORMED_EXCEPTION);
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            log.error(Constants.JWT_ILLEGAL_ARGUMENT_EXCEPTION);
            return Optional.empty();
        }
    }

//    ExpiredJwtException
//UnsupportedJwtException
//MalformedJwtException
//SignatureException
//IllegalArgumentException

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        log.info("Expiration" + expiration);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
