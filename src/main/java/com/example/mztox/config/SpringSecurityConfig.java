package com.example.mztox.config;

import com.example.mztox.filter.JwtAuthenticationFilter;
import com.example.mztox.provider.JwtAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity // Spring Security를 활성화합니다.
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성합니다.
public class SpringSecurityConfig {

    private final JwtAuthProvider jwtAuthProvider; // JWT 인증을 제공하는 클래스

    // AuthenticationManager 빈을 생성합니다.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // PasswordEncoder 빈을 생성합니다. BCryptPasswordEncoder를 사용하여 비밀번호를 암호화합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS 설정을 정의하는 빈을 생성합니다.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // 모든 도메인 허용
        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        configuration.setMaxAge(3600L); // pre-flight 요청의 캐시 시간
        configuration.setAllowCredentials(false); // 자격 증명을 허용하지 않음
        configuration.addExposedHeader("accesstoken"); // 노출할 헤더 설정
        configuration.addExposedHeader("content-disposition");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용
        return source;
    }

    // 보안 필터 체인을 정의하는 빈을 생성합니다.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리를 Stateless 방식으로 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/signup", "/login/**", "/exception/**").permitAll() // 특정 경로에 대한 접근 허용
                        .anyRequest().hasRole("USER") // 그 외의 모든 요청은 USER 역할이 필요
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new CustomAccessDeniedPoint()) // 접근 거부 처리기 설정
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 실패 처리기 설정
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtAuthProvider), UsernamePasswordAuthenticationFilter.class); // JWT 인증 필터 추가

        return http.build(); // 보안 설정을 빌드하여 반환
    }
}
