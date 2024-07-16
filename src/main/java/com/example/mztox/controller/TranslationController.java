package com.example.mztox.controller;

import com.example.mztox.dto.TranslationRequest;
import com.example.mztox.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
// 주어진 신조어를 번역하기 위해 AI 서버와 통신하고, 번역된 표준어를 클라이언트에게 반환
@RestController
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    @PostMapping("/main")
    //@RequestBody -> HTTP 요청 본문을 String 타입의 slang 파라미터로 변환
    public ResponseEntity<String> translate(@RequestBody TranslationRequest translationRequest){
        String standard = translationService.translateSlang(translationRequest.getSlang());
        return ResponseEntity.ok(standard);
    }
}
