package com.example.mztox.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtAuthProvider {

    @Value("${spring.jwt.secret.at}")
    private String atSecretKey;

    @PostConstruct
    protected void init() {
        atSecretKey = Base64.getEncoder().encodeToString(atSecretKey.getBytes());
    }

    private final UserDetailsService userDetailsService;

    public String createToken(long userPk, String email) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userPk))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 3600000)) // 1시간
                .claim("email", email)
                .signWith(SignatureAlgorithm.HS256, atSecretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(atSecretKey).parseClaimsJws(token).getBody();
        String email = claims.get("email", String.class);
        return new UsernamePasswordAuthenticationToken(email, "", null);
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("accessToken");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(atSecretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
