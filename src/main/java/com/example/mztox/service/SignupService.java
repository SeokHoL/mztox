package com.example.mztox.service;

import com.example.mztox.dto.SignupDto;
import com.example.mztox.entity.Members;
import com.example.mztox.exception.UserNotFoundException;
import com.example.mztox.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignupService {

    private final MemberRepository memberRepository; // 회원 정보 저장소를 주입받습니다.
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 인코더를 주입받습니다.
    private final ModelMapper modelMapper; // DTO와 엔티티 간의 변환을 위한 라이브러리를 주입받습니다.
    private static final Logger logger = LoggerFactory.getLogger(SignupService.class);

    // 회원 가입 메서드
    public void signup(SignupDto signupDto) throws Exception {

        if (isDuplicateMember(signupDto)){
            throw  new Exception("중복된 email이 있습니다.");
        }
        // SignupDto를 Members 엔티티로 변환합니다.
        Members member = modelMapper.map(signupDto, Members.class);

        // 비밀번호를 암호화하여 엔티티에 설정합니다.
        member.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        // 회원 정보를 데이터베이스에 저장합니다.
        memberRepository.save(member);
    }
    //중복 회원 정보 확인 메서드
    public boolean isDuplicateMember(SignupDto signupDto) {
        return  memberRepository.existsByEmail(signupDto.getEmail());
    }
    public void deleteMemberByEmailAndPassword(String email, String password) throws UserNotFoundException {
        logger.info("Attempting to delete user with email: {}", email);
        Members user = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Password mismatch for user with email: {}", email);
            throw new UserNotFoundException("유효하지 않은 이메일 또는 비밀번호");
        }

        memberRepository.delete(user);
        logger.info("User with email: {} deleted successfully", email);
    }


}