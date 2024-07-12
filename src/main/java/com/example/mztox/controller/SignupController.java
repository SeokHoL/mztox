package com.example.mztox.controller;


import com.example.mztox.dto.SignupDto;
import com.example.mztox.service.SignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


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
    /*유효성 검사가 실패하면 MethodArgumentNotValidException 예외가 자동으로 발생하고,
    이 예외를 처리하는 핸들러 메서드(handleValidationExceptions)가 자동으로 호출됩니다.
    이는 스프링 프레임워크의 예외 처리 메커니즘 덕분이다.*/
    // 유효성 검증 실패 예외 처리
    // MethodArgumentNotValidException 예외를 처리하기 위한 핸들러 메서드를 지정합니다.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        //Map: 키와 값의 쌍을 저장하는 데이터 구조를 나타내는 인터페이스 이다.
        //HashMap은 클래스임. Map 인터페이스를 구현하는 클래스다.
        Map<String, String> errors = new HashMap<>(); // 유효성 검증 실패로 발생한 오류 메시지를 저장할 맵을 생성합니다.
        ex.getBindingResult().getAllErrors().forEach((error) -> { // 유효성 검증 오류 리스트를 순회합니다.
            String fieldName = ((FieldError) error).getField(); // 오류가 발생한 필드 이름을 가져옵니다.
            String errorMessage = error.getDefaultMessage(); // 유효성 검증 실패 시 설정한 기본 오류 메시지를 가져옵니다.
            errors.put(fieldName, errorMessage); // 필드 이름을 키로, 오류 메시지를 값으로 하여 맵에 추가합니다.
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors); // HTTP 상태 코드 400 (Bad Request)와 함께 오류 메시지를 응답 본문으로 반환합니다.
    }
}

//    // 기타 예외 처리
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGeneralExceptions(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예기치 않은 오류가 발생했습니다. 나중에 다시 시도해 주세요.");
//    }
//