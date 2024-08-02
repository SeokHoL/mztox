package com.example.mztox.service;
// AI 서버와 통신하고 번역된 결과를 DB에 저장하는 서비스 클래스를 작성

import com.example.mztox.dto.TranslationItem;
import com.example.mztox.dto.TranslationRequest;
import com.example.mztox.dto.TranslationResponse;
import com.example.mztox.entity.Translation;
import com.example.mztox.repository.TranslationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

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
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("content", translationRequest.getSlang());
        String requestJson = jsonNode.toString();
        // HttpEntity 객체에 헤더와 바디를 함께 설정
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        //커밋용
        TranslationResponse response = restTemplate.postForObject(aiServerUrl, entity, TranslationResponse.class);
//        System.out.println(response.getContent());
        // 번역된 결과를 DB에 저장
        Translation translation = Translation.builder()
                .email(email)
                .slang(translationRequest.getSlang())
                .standard(response.getContent())
                .build();
        translationRepository.save(translation);

        return response.getContent();
    }

    //최근 번역기록을 가져오는 메서드
    public TranslationResponse getRecentTranslations(String email) {
        Pageable pageable = PageRequest.of(0, 5);
        List<Translation> translations = translationRepository.findTopBy5ByUserIdOrderByCreatedDateDesc(email,pageable);

        List<TranslationItem> items  = translations.stream()
                //t는 스트림의 현재 요소를 나타내는 Translation 객체입니다.
                .map(t ->new TranslationItem(t.getSlang(),t.getStandard()))
                .collect(Collectors.toList());

        TranslationResponse response = new TranslationResponse();
        response.setItemLen(items.size());
        response.setItems(items);

        return response;
    }
}