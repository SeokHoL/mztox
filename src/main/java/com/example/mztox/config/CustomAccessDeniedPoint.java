package com.example.mztox.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
//이 클래스는 Spring Security에서 사용자가 접근 권한이 없는 자원에 접근하려고 할 때 처리하는 커스텀 접근 거부 처리기
@Component
public class CustomAccessDeniedPoint implements AccessDeniedHandler {

    /*이 메서드는 지정된 상태 코드와 메시지를 사용하여 HTTP 응답을 보냅니다.
    HttpServletResponse.SC_FORBIDDEN: HTTP 상태 코드 403을 나타냅니다.
    이는 "Forbidden"을 의미하며, 사용자가 요청한 리소스에 대한 접근 권한이 없음을 나타냅니다.*/
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }
}