package com.example.mztox.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


//회원 가입 요청 시 클라이언트가 제공하는 회원 정보를 담는 DTO

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {

    @NotBlank(message = "'email' is a required input value") //빈칸을 허용하지않음
    @Email(message = "It is not in email format") //이메일 형식이어야함 ex) @gmail.com
    private String email;

    @NotBlank(message = "'password' is a required input value")
    private String password;

    @NotBlank(message = "'name' is a required input value")
    private String name;

    @NotBlank(message = "'mobile' is a required input value")
    private String mobile;
}
