package com.piotrpabich.talktactics.auth.token;

import com.piotrpabich.talktactics.auth.AuthConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class TokenServiceImpl implements TokenService {
    private final UserDetailsService userDetailsService;
    private final TokenConfig tokenConfig;

//  PUBLIC
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
    @Override
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, tokenConfig.getJwtExpiration());
    }
    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, tokenConfig.getRefreshExpiration());
    }
    @Override
    public Optional<UserDetails> validateToken(String token) {
        try {
            String username = extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
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

//  PRIVATE
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
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
        byte[] keyBytes = Decoders.BASE64.decode(tokenConfig.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
