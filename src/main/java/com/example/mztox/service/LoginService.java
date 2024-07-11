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
        logger.info("Login attempt for email: {}", loginDto.getEmail());
        Members member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> {
                    logger.error("User not found for email: {}", loginDto.getEmail());
                    return new UserNotFoundException("User Not Found");
                });

        logger.info("User found: {}", member.getEmail());
        logger.info("Entered password: {}", loginDto.getPassword());
        logger.info("Stored password: {}", member.getPassword());

        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            logger.error("Password mismatch for email: {}", loginDto.getEmail());
            throw new ForbiddenException("Passwords do not match");
        }

        logger.info("Login successful for email: {}", loginDto.getEmail());
        return modelMapper.map(member, AuthenticationDto.class);
    }
}
