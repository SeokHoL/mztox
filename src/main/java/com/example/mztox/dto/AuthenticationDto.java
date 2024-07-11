package com.example.mztox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDto {

    private Long id;
    private String email;
    private String password;
    private String mobile;
    private String name;

    private String regDate;
    private String modDate;

}
