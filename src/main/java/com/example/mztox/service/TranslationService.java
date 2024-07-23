package com.example.mztox.service;
// AI 서버와 통신하고 번역된 결과를 DB에 저장하는 서비스 클래스를 작성

import com.example.mztox.dto.TranslationRequest;
import com.example.mztox.dto.TranslationResponse;
import com.example.mztox.entity.Translation;
import com.example.mztox.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TranslationService {
    private final TranslationRepository translationRepository;
    private final RestTemplate restTemplate;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    public String translateSlang(TranslationRequest translationRequest, String email) {
        // AI 서버로 신조어 번역 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = "{\"content\":\"" + translationRequest.getSlang() + "\"}";

        //이 설정은 HTTP 요청을 생성할 때, 요청 바디와 헤더를 함께 설정하기 위해 사용
        //의미: HttpEntity 객체를 생성하여 요청 바디와 헤더를 함께 포함한 HTTP 요청을 만듬.
        /*HttpEntity 객체는 RestTemplate을 사용하여 AI 서버로 HTTP 요청을 보낼 때 사용됩니다.
        이 설정을 통해 서버는 요청 바디와 헤더를 함께 수신하고, 적절하게 처리할 수 있습니다.*/
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        
        //TranslationResponse dto로 변환
        TranslationResponse response = restTemplate.postForObject(aiServerUrl, entity, TranslationResponse.class);

        // 번역된 결과를 DB에 저장
        //DTO인 TranslationResponse는 AI 서버에서 받은 번역 결과를 담고 있으며,
        // 이 데이터를 데이터베이스에 저장하기 위해 Translation entity로 변환
        Translation translation = Translation.builder()
                .email(email)
                .slang(translationRequest.getSlang())
                .standard(response.getContent())
                .build();
        translationRepository.save(translation);

        return response.getContent();
    }
}
