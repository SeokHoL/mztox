package com.example.mztox.controller;

import com.example.mztox.dto.AuthenticationDto;
import com.example.mztox.dto.LoginDto;
import com.example.mztox.dto.TranslationResponse;
import com.example.mztox.exception.ForbiddenException;
import com.example.mztox.exception.UserNotFoundException;
import com.example.mztox.provider.JwtAuthProvider;
import com.example.mztox.service.LoginService;
import com.example.mztox.service.TranslationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor // 생성자를 통해 의존성을 주입받기 위한 Lombok 어노테이션
@RestController // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냄
@Validated // 유효성 검사를 활성화
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService; // 로그인 서비스 빈을 주입받음
    private final JwtAuthProvider jwtAuthProvider; // JWT 인증 제공자 빈을 주입받음
    private final TranslationService translationService;

    @PostMapping("/login") // HTTP POST 요청을 "/login" 경로로 매핑
    public ResponseEntity<?> appLogin(@Valid @RequestBody LoginDto loginDto) {
        try {

            // 로그인 서비스로 로그인 시도
            AuthenticationDto authentication = loginService.loginService(loginDto);

            // JWT 토큰 생성
            String token = jwtAuthProvider.createToken(authentication.getId(), authentication.getEmail());

            //최근 번역기록을 가져옴.
            TranslationResponse translations = translationService.getRecentTranslations(authentication.getEmail());


            // 응답 데이터 준비
            //Map
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("name", authentication.getName());
            responseBody.put("itemLen", translations.getItemLen());
            responseBody.put("items", translations.getItems()); // slang과 standard 쌍으로 구성된 리스트


            // 응답 헤더에 토큰 포함하여 반환
            return ResponseEntity.ok()
                    .header("accesstoken", token)
                    .body(responseBody);
//                    .build();
        } catch (UserNotFoundException e) {
            // 사용자를 찾지 못한 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
//                    .body(e.getMessage()); // 401 상태 코드와 예외 메시지 반환
        } catch (ForbiddenException e) {
            // 비밀번호가 일치하지 않는 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
//                    .body(e.getMessage()); // 403 상태 코드와 예외 메시지 반환
        } catch (Exception e) {
            // 기타 예상치 못한 오류 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
//                    .body("Unexpected error occurred"); // 500 상태 코드와 일반 오류 메시지 반환
        }
    }



}