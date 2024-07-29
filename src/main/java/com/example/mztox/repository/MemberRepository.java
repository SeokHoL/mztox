package com.example.mztox.repository;

import com.example.mztox.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Members, Long> {

    // 이메일을 기반으로 Members 엔티티를 조회하는 커스텀 쿼리 메서드
    Optional<Members> findByEmail(String email);


    boolean existsByEmail(String email);
}
