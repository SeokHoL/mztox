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

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public void signup(SignupDto signupDto) {
        Members member = modelMapper.map(signupDto, Members.class);
        member.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        memberRepository.save(member);
    }
}
