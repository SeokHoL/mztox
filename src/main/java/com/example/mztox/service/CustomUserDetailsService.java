package com.example.mztox.service;

import com.example.mztox.entity.CustomUserDetails;
import com.example.mztox.entity.Members;
import com.example.mztox.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor // Lombok 어노테이션으로, final 필드에 대한 생성자를 자동으로 생성합니다.
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository; // 회원 정보를 저장하고 관리하는 리포지토리 인터페이스입니다.

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 주어진 이메일로 회원 정보를 조회합니다.
        Members member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // 조회된 회원 정보를 바탕으로 CustomUserDetails 객체를 생성하여 반환합니다.
        return new CustomUserDetails(member.getId(), member.getEmail());
    }
}
