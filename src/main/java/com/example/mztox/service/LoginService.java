package com.example.mztox.service;

import com.example.mztox.dto.AuthenticationDto;
import com.example.mztox.dto.LoginDto;
import com.example.mztox.entity.Members;
import com.example.mztox.exception.ForbiddenException;
import com.example.mztox.exception.UserNotFoundException;
import com.example.mztox.provider.JwtAuthProvider;
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

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class); // 로깅을 위한 로거 인스턴스

    private final MemberRepository memberRepository; // 회원 정보 저장소를 주입받습니다.
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 인코더를 주입받습니다.
    private final ModelMapper modelMapper; // DTO와 엔티티 간의 변환을 위한 라이브러리를 주입받습니다.
    private final JwtAuthProvider jwtAuthProvider; // JWT 인증 제공자를 주입받습니다.

    // 로그인 서비스 메서드
    public AuthenticationDto loginService(LoginDto loginDto) {

        // 이메일로 회원 정보를 조회합니다.
        Members member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> {
                    return new UserNotFoundException("User Not Found");
                });

        // 입력된 비밀번호가 저장된 비밀번호와 일치하는지 확인합니다.
        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new ForbiddenException("Passwords do not match");
        }

        // ModelMapper를 사용하여 Members 엔티티를 AuthenticationDto로 변환하여 반환합니다.
        AuthenticationDto authenticationDto = modelMapper.map(member, AuthenticationDto.class);
        authenticationDto.setName(member.getName());
        return authenticationDto;
    }
}
