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
    //DTO(Data Transfer Object)와 엔티티(Entity) 간의 변환을 쉽게 하기 위해 사용되는 라이브러리
    private final ModelMapper modelMapper;

    public String signup(SignupDto signupDto) {

        if (memberRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            return "똑같은 이메일이 있습니다.";
        }
            Members member = modelMapper.map(signupDto, Members.class);
            member.setPassword(passwordEncoder.encode(signupDto.getPassword()));
            memberRepository.save(member);

            return ;
        }
    }

