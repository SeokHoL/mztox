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
@RequestMapping("/api")
@Validated
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;
    private final JwtAuthProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationDto> appLogin(@Valid @RequestBody LoginDto loginDto) throws Exception {
        logger.info("Signin attempt for email: {}", loginDto.getEmail());
        AuthenticationDto authentication = loginService.loginService(loginDto);
        String token = jwtProvider.createToken(authentication.getId(), authentication.getEmail());
        logger.info("Signin successful for email: {}. Token generated.", loginDto.getEmail());

        return ResponseEntity.ok()
                .header("accesstoken", token)
                .body(authentication);
    }
}
