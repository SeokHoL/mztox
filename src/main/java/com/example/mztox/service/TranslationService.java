    package com.example.mztox.service;
    //AI 서버와 통신하고 번역된 결과를 DB에 저장하는 서비스 클래스를 작성

    import com.example.mztox.dto.TranslationRequest;
    import com.example.mztox.entity.Translation;
    import com.example.mztox.repository.TranslationRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestTemplate;

    @Service
    @RequiredArgsConstructor
    public class TranslationService {
        private final TranslationRepository translationRepository;
        private final RestTemplate restTemplate;

        @Value("${ai.server.url}")
        private String aiServerUrl;

        public  String translateSlang(TranslationRequest translationRequest){
            //AI 서버로 신조어 번역 요청
            //restTemplate은 Spring에서 제공하는 HTTP 클라이언트로, RESTful 웹 서비스와 상호작용하는 데 사용됩니다.
            //postForObject 메서드는 주어진 URL로 HTTP POST 요청을 보내고, 응답을 객체로 변환하여 반환합니다.
            //세게의 인자를 줘야됨. (요청보낼 Url, 요청보낼 데이터, 응답받은 타입(String, Int 등)
            String standard = restTemplate.postForObject(aiServerUrl,translationRequest.getSlang(),String.class);

            //번역된 결과를 DB에 저장
            Translation translation = Translation.builder()
                    .email(translationRequest.getEmail())
                    .slang(translationRequest.getSlang())
                    .standard(standard)
                    .build();
            translationRepository.save(translation);

            return standard;

        }



    }
