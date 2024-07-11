package com.example.mztox.service;

import com.example.mztox.dto.AuthenticationDto;
import com.example.mztox.dto.LoginDto;
import com.example.mztox.entity.Members;
import com.example.mztox.exception.ForbiddenException;
import com.example.mztox.exception.UserNotFoundException;
import com.example.mztox.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public AuthenticationDto loginService(LoginDto loginDto) {

        Members member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> {

                    return new UserNotFoundException("User Not Found");
                });



        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new ForbiddenException("Passwords do not match");
        }

        return modelMapper.map(member, AuthenticationDto.class);
        //modelMapper를 사용하여 Members 엔티티를 -> AuthenticationDto로 변환하여 반환
    }
}
