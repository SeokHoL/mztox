package com.example.mztox.filter;

import com.example.mztox.provider.JwtAuthProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;


import java.io.IOException;
//이 클래스는 Spring Security 필터 체인에서 JWT 토큰을 기반으로 인증을 수행하는 필터 역할을 합니다
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    //JwtAuthProvider는 JWT 토큰의 생성 및 검증을 담당하는 클래스
    private final JwtAuthProvider jwtAuthProvider;

    @Override
    //ServletRequest와 ServletResponse는 일반적인 서블릿 요청과 응답 객체입니다.
    //FilterChain은 필터 체인을 나타내며, 필터가 요청과 응답을 처리하고 다음 필터로 전달할 수 있도록 합니다.
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //ServletRequest와 ServletResponse를 각각 HttpServletRequest와 HttpServletResponse로 형변환합니다.
        // 이는 HTTP 관련 메서드에 접근하기 위해 필요합니다.
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        //요청 메서드가 OPTIONS인지 확인합니다. OPTIONS 요청은 일반적으로 CORS(Cross-Origin Resource Sharing) 프리플라이트 요청으로 사용됩니다.
        //OPTIONS 요청인 경우, HTTP 상태 코드를 200(OK)으로 설정하여 응답합니다. 이는 프리플라이트 요청을 처리하기 위함입니다.
        if ("OPTIONS".equalsIgnoreCase(httpReq.getMethod())) {
            httpRes.setStatus(HttpServletResponse.SC_OK);
        } else {
            //jwtAuthProvider.resolveToken(httpReq) 메서드를 호출하여 HttpServletRequest 객체에서 JWT 토큰을 추출합니다.
            String token = jwtAuthProvider.resolveToken(httpReq);
            //jwtAuthProvider.validateToken(token) 메서드를 호출하여 토큰의 유효성을 검증합니다.
            // 이 메서드는 토큰이 만료되었는지, 서명이 유효한지 등을 검사
            if (token != null && jwtAuthProvider.validateToken(token)) {
                //이 메서드는 JWT 토큰에서 사용자 정보를 추출하고, 해당 정보를 기반으로 Authentication 객체를 생성합니다.
                Authentication auth = jwtAuthProvider.getAuthentication(token);
                //SecurityContextHolder.getContext().setAuthentication(auth) 메서드를 호출하여 생성된 Authentication 객체를 SecurityContext에 설정
                //securityContext는 현재 스레드와 관련된 인증 정보를 저장하는 컨테이너
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            //이 메서드를 호출함으로써 현재 필터에서 처리한 요청과 응답이 다음 필터로 전달됩니다.
            //만약 필터 체인의 끝에 도달하면, 요청은 최종적으로 서블릿으로 전달되어 실제 비즈니스 로직이 실행됩니다.
            filterChain.doFilter(request, response);
        }
    }


}
