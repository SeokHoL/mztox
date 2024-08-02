package com.example.mztox.controller;

import com.example.mztox.dto.TranslationRequest;
import com.example.mztox.provider.JwtAuthProvider;
import com.example.mztox.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// 주어진 신조어를 번역하기 위해 AI 서버와 통신하고, 번역된 표준어를 클라이언트에게 반환
@RestController
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;
    private final JwtAuthProvider jwtAuthProvider;

    @PostMapping("/main")
    public ResponseEntity<Map<String,String>> translate(@RequestHeader("accesstoken") String accesstoken, @RequestBody TranslationRequest translationRequest) {
        // 토큰에서 이메일 추출
        String emailFromToken = jwtAuthProvider.getEmailFromToken(accesstoken);

        String standard = translationService.translateSlang(translationRequest, emailFromToken);
        Map<String, String> response = new HashMap<>();
        response.put("standard", standard);

        return ResponseEntity.ok(response);
    }
}