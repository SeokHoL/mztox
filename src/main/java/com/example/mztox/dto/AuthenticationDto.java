package com.example.mztox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//회원인증정보를 담는 DTO
//로그인 후 사용자 정보를 클라이언트에게 반환할 때 사용
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDto {

    private Long id;
    private String email;
    private String name;
//    private String token;


}