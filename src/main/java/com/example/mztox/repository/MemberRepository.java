package com.example.mztox.repository;

import com.example.mztox.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Members, Long> {
    Optional<Members> findByEmail(String email);
}
