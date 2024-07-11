package com.example.mztox.controller;


import com.example.mztox.dto.SignupDto;
import com.example.mztox.service.SignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
//@RequestMapping("/api")
@Validated //유효성 검사를 활성화
public class SignupController {

    private final SignupService signupService;

    @PostMapping("/signup")
    //객체로 매핑하고 유효성 검사를 수행합니다. 이뜻은 밑에를 참조
    //@RequestBody와 @Valid 어노테이션을 사용하여 클라이언트로부터 전달받은 JSON 데이터를 LoginDto 객체로 변환하고,
    // 이 객체에 정의된 유효성 검사 규칙을 적용하여 입력 데이터가 올바른지 검증한다.
    //@RequestBody -> HTTP 요청의 본문에 JSON 형식으로 데이터를 보내면 ->Java 객체로 변환
    //@Valid 어노테이션은 해당 객체에 대해 유효성 검사를 수행하도록 지시
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupDto signupDto) {
        signupService.signup(signupDto);
        return ResponseEntity.ok().build();
    }
}
