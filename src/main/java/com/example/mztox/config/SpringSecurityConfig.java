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

@Configuration //Spring의 설정 클래스를 나타냅니다.
@EnableWebSecurity  //Spring Security 설정을 활성화
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtAuthProvider jwtAuthProvider;

    // AuthenticationManager 빈 생성 -> springboot에서 이미 만들어져있음.
    //AuthenticationManager는 Spring Security에서 인증 작업을 처리하는 주요 인터페이스입니다. 주어진 인증 요청에 대해 인증을 수행하고,
    // 성공 또는 실패 결과를 반환합니다.
    //AuthenticationConfiguration  클래스는 AuthenticationManager와 관련된 설정을 제공하며,
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    //PasswordEncoder 가 더 상위
    //PasswordEncoder는 비밀번호를 암호화하고, 암호화된 비밀번호와 평문 비밀번호를 비교하는 기능을 제공하는 인터페이스
    //BCryptPasswordEncoder는 PasswordEncoder 인터페이스를 구현한 클래스 중 하나로, bcrypt 해시 함수를 사용하여 비밀번호를 암호화 함.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //CORS(Cross-Origin Resource Sharing) 설정을 정의하여, 애플리케이션이 외부 도메인으로부터의 요청을 허용할 수 있도록
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration(); //CorsConfiguration 객체를 생성합니다. 이 객체는 CORS 설정을 정의하는 데 사용
        configuration.addAllowedOrigin("*"); //모든 도메인에서의 요청을 허용, 만약 특정 도메인만 허용하고 싶다면 "http://example.com" 같은 형태로 도메인을 지정
        configuration.addAllowedMethod("*"); //모든 HTTP 메서드(GET, POST, PUT, DELETE 등)를 허용,  특정 메서드만 허용하고 싶다면 "GET" 또는 "POST" 같은 형태로 지정
        configuration.addAllowedHeader("*"); // 모든 헤더를 허용 ,특정 헤더만 허용하고 싶다면 "Authorization" 또는 "Content-Type" 같은 형태로 지정
        configuration.setMaxAge(3600L); //프리플라이트(pre-flight) 요청의 유효 기간을 설정, 실제 요청을 보내기전 무슨 사전요청?이라네
        configuration.setAllowCredentials(false); //자격 증명(쿠키, 인증 헤더 등)을 허용할지 여부를 설정
        configuration.addExposedHeader("accessToken"); //클라이언트가 응답 헤더 중 accessToken 헤더를 접근할 수 있도록 합니다.
        configuration.addExposedHeader("content-disposition"); //클라이언트가 응답 헤더 중 content-disposition 헤더를 접근할 수 있도록 합니다.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); //이 객체는 URL 패턴에 따라 CORS 설정을 적용하는 데 사용
        source.registerCorsConfiguration("/**", configuration); //모든 경로(/**)에 대해 앞서 정의한 CORS 설정을 적용
        // 모든 엔드포인트("/**")에 대해 정의된 CorsConfiguration 설정을 적용하는 역할
        return source;
    }

    //HttpSecurity 객체를 통해 보안 설정을 구성하고, http.build()를 호출하여 SecurityFilterChain 객체를 생성합니다
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)//비활성화, REST API에서는 주로 토큰 기반 인증을 사용하기 때문에 CSRF 보호가 필요하지 않습니다.
                //서버가 세션을 유지하지 않고, 각 요청이 독립적으로 처리됨을 의미 STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //특정 엔드포인트에 대한 접근 권한을 설정
                .authorizeHttpRequests(authorize -> authorize
                        ///signup, /signin/**, /exception/** 경로는 모든 사용자에게 허용
                        .requestMatchers("/signup", "/signin/**", "/main/**").permitAll()
                        //그 외의 모든 요청은 "USER" 역할을 가진 사용자만 접근할 수 있음
                        .anyRequest().hasRole("USER")
                )
                //corsConfigurationSource() 메서드는 CORS 설정을 반환
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                //예외 처리 핸들러를 설정
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new CustomAccessDeniedPoint()) //접근이 거부되었을 때의 처리.
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) //인증이 필요할 때의 처리.
                )
                //JwtAuthenticationFilter를 Spring Security 필터 체인에 추가
                //UsernamePasswordAuthenticationFilter 앞에 배치하여 JWT를 통한 인증을 먼저 처리
                //JWT 토큰을 사용한 인증이 우선적으로 처리되고, JWT가 유효한 경우 사용자 정보가 SecurityContext에 설정됩니다. 이후의 인증 로직(예: email 과 비밀번호 인증)은 건너뛰게 됩니다.
                .addFilterBefore(new JwtAuthenticationFilter(jwtAuthProvider), UsernamePasswordAuthenticationFilter.class);

        //구성된 HttpSecurity 객체를 빌드하여 SecurityFilterChain 객체를 생성하고 반환
        return http.build();
    }
}
