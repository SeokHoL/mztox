package com.example.mztox.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {

    @NotBlank(message = "'email' is a required input value")
    @Email(message = "It is not in email format")
    private String email;

    @NotBlank(message = "'password' is a required input value")
    private String password;

    @NotBlank(message = "'name' is a required input value")
    private String name;

    @NotBlank(message = "'mobile' is a required input value")
    private String mobile;
}
