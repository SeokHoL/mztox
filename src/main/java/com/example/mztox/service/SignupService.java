package com.example.mztox.service;

import com.example.mztox.dto.SignupDto;
import com.example.mztox.entity.Members;
import com.example.mztox.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignupService {

    private final MemberRepository memberRepository; // 회원 정보 저장소를 주입받습니다.
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 인코더를 주입받습니다.
    private final ModelMapper modelMapper; // DTO와 엔티티 간의 변환을 위한 라이브러리를 주입받습니다.

    // 회원 가입 메서드
    public void signup(SignupDto signupDto) {
        // SignupDto를 Members 엔티티로 변환합니다.
        Members member = modelMapper.map(signupDto, Members.class);

        // 비밀번호를 암호화하여 엔티티에 설정합니다.
        member.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        // 회원 정보를 데이터베이스에 저장합니다.
        memberRepository.save(member);
    }
}
