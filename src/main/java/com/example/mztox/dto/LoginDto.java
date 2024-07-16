package com.example.mztox.dto;

import com.example.mztox.entity.Members;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    private String email;
    private String password;

    // LoginDto를 Members 엔티티로 변환하는 메서드
    public Members toEntity() {
        return Members.builder()
                .email(email)
                .password(password)
                .build();
    }
}
