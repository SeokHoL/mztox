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

    @NotBlank(message = "email은 필수 작성입니다.") //빈칸을 허용하지않음
    @Email(message = "올바른 이메일형식 이어야 합니다. ex)test@gmail.com") //이메일 형식이어야함 ex) @gmail.com
    private String email;

    @NotBlank(message = "password은 필수 작성입니다.")
    private String password;

    @NotBlank(message = "name은 필수 작성입니다.")
    private String name;

    @NotBlank(message = "mobile은 필수 작성입니다.")
    private String mobile;
}
