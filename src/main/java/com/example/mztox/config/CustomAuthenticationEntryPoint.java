package com.example.mztox.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;
/*이 클래스는 Spring Security에서 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 처리하는 커스텀 인증 진입점을 정의*/
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /*이 메서드는 지정된 상태 코드와 메시지를 사용하여 HTTP 응답을 보냅니다.
    HttpServletResponse.SC_UNAUTHORIZED: HTTP 상태 코드 401을 나타냅니다.
    이는 "Unauthorized"를 의미하며, 사용자가 인증되지 않았음을 나타냅니다.
    "Unauthorized": 클라이언트에게 전달할 오류 메시지입니다.*/
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}