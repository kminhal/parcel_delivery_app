package com.delivery.orderservice.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    final String AUTHORITIES = "auth";
    final String FIRST_NAME = "first_name";
    final String LAST_NAME = "last_name";

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    public boolean isValidToken(String jwt) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT signature", e);
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("Invalid JWT Token", e);
        }
        return false;
    }

    public Authentication parseAuthentication(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetailsImpl userDetails = new UserDetailsImpl(
                claims.getSubject(),
                "",
                claims.get(FIRST_NAME, String.class),
                claims.get(LAST_NAME, String.class),
                authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, jwt, authorities);
    }
}