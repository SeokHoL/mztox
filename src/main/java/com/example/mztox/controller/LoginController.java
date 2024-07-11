package com.example.mztox.controller;

import com.example.mztox.dto.AuthenticationDto;
import com.example.mztox.dto.LoginDto;
import com.example.mztox.provider.JwtAuthProvider;
import com.example.mztox.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@RestController
//@RequestMapping("/api")
@Validated //유효성 검사를 활성화
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;
    private final JwtAuthProvider jwtProvider;

    @PostMapping("/signin")
    //클라이언트로부터JSON을 -> LoginDto 객체로 변환
    public ResponseEntity<AuthenticationDto> appLogin(@Valid @RequestBody LoginDto loginDto) throws Exception {
        AuthenticationDto authentication = loginService.loginService(loginDto);

        // JWT 토큰 생성
        String token = jwtProvider.createToken(authentication.getId(), authentication.getEmail());

        // DTO에 토큰 설정 , 생성된 토큰을 AuthenticationDto 객체에 설정
        authentication.setToken(token);

        // 응답 헤더와 본문에 토큰 포함
        return ResponseEntity.ok()
                .header("accessToken", token)
                .body(authentication);
    }
}
