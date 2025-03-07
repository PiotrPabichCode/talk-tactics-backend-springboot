package com.piotrpabich.talktactics.auth.token;

import com.piotrpabich.talktactics.auth.AuthConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class TokenService {

    private final UserDetailsService userDetailsService;
    private final TokenConfig tokenConfig;

    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(final UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            final Map<String, Object> extraClaims,
            final UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, tokenConfig.getJwtExpiration());
    }

    public String generateRefreshToken(final UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, tokenConfig.getRefreshExpiration());
    }

    public Optional<UserDetails> validateToken(final String token) {
        try {
            final var username = extractUsername(token);
            final var userDetails = userDetailsService.loadUserByUsername(username);
            if(!username.equals(userDetails.getUsername()) || isTokenExpired(token)) {
                return Optional.empty();
            }
            return Optional.of(userDetails);
        } catch (UsernameNotFoundException e) {
            log.error(AuthConstants.USERNAME_NOT_FOUND_EXCEPTION);
            return Optional.empty();
        } catch (ExpiredJwtException e) {
            log.error(AuthConstants.JWT_EXPIRED_EXCEPTION);
            return Optional.empty();
        } catch (SignatureException e) {
            log.error(AuthConstants.JWT_SIGNATURE_EXCEPTION);
            return Optional.empty();
        } catch (UnsupportedJwtException e) {
            log.error(AuthConstants.JWT_UNSUPPORTED_EXCEPTION);
            return Optional.empty();
        } catch (MalformedJwtException e) {
            log.error(AuthConstants.JWT_MALFORMED_EXCEPTION);
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            log.error(AuthConstants.JWT_ILLEGAL_ARGUMENT_EXCEPTION);
            return Optional.empty();
        }
    }

    private String buildToken(
            final Map<String, Object> extraClaims,
            final UserDetails userDetails,
            final long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(final String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        final var keyBytes = Decoders.BASE64.decode(tokenConfig.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
