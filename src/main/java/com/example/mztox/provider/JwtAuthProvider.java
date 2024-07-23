package com.example.mztox.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/* JwtAuthProvider 클래스는 JWT(JSON Web Token)를 생성, 검증 및 인증 정보를 처리하는 기능을 제공합니다.
   이 클래스는 Spring의 빈으로 등록되어 애플리케이션 전역에서 사용될 수 있습니다.
   JWT는 사용자 인증과 권한 부여에 사용되는 토큰 기반 인증 방식입니다. */

@RequiredArgsConstructor
@Component // Spring의 빈으로 등록하여 애플리케이션 전역에서 사용할 수 있게 합니다.
public class JwtAuthProvider {

    @Value("${spring.jwt.secret.at}")
    private String atSecretKey;

    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        this.secretKey = Keys.hmacShaKeyFor(atSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    private final UserDetailsService userDetailsService;

    // JWT 토큰을 생성하는 메서드
    public String createToken(long userPk, String email) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userPk)) // 사용자 식별자를 서브젝트로 설정
                .setIssuedAt(now) // 토큰 발행 시간 설정
                .setExpiration(new Date(now.getTime() + 3600000)) // 만료 시간 설정 (1시간)
                .claim("email", email) // 추가 클레임 설정 , 추가정보로 생각.
                .signWith(secretKey, SignatureAlgorithm.HS256) // 서명 알고리즘과 secretKey를 사용하여 서명
                .compact(); // 토큰 생성
    }

    // JWT 토큰으로부터 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String email = claims.get("email", String.class); // 토큰에서 이메일 정보 추출
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()); // 인증 객체 생성
    }

    // jwt토큰에서 이메일을 추출하는 메서드
    public String getEmailFromToken(String accesstoken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(accesstoken)
                .getBody();
        return claims.get("email", String.class);
    }

    // HTTP 요청에서 토큰을 추출하는 메서드
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("accesstoken");
    }

    // 토큰의 유효성을 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token); // 토큰 검증
            return true; // 검증 성공 시 true 반환
        } catch (Exception e) {
            return false; // 검증 실패 시 false 반환
        }
    }
}
