package com.example.mztox.controller;

import com.example.mztox.dto.TranslationRequest;
import com.example.mztox.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// 주어진 신조어를 번역하기 위해 AI 서버와 통신하고, 번역된 표준어를 클라이언트에게 반환
@RestController
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    @PostMapping("/main")
    //@RequestBody -> HTTP 요청 본문을 TranslationRequest 객체로 변환합니다.
    public ResponseEntity<Map<String, String>> translate(@RequestBody TranslationRequest translationRequest){
        String standard = translationService.translateSlang(translationRequest);

        //응답형태를 json으로 만들기위해 Map을 사용
//        return ResponseEntity.ok(standard); -> 이렇게하면 단순 text형태로 응답이 감.
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("standard", standard);

        return ResponseEntity.ok(responseBody);
    }
}
