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
@RequestMapping("/api")
@Validated
public class SignupController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupDto signupDto) {
        signupService.signup(signupDto);
        return ResponseEntity.ok().build();
    }
}
