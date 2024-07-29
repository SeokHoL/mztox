package com.example.mztox.repository;

import com.example.mztox.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TranslationRepository extends JpaRepository<Translation,Long> {


    @Query("SELECT t FROM  Translation t WHERE  t.email =:email ORDER BY t.createdDate DESC")
    //:userId는 메서드의 @Param("userId")로 전달된 값과 매핑됩니다.
    //findTopBy5ByUserIdOrderByCreatedDateDesc 정의함으로써 Spring Data JPA가 자동으로 쿼리에 LIMIT 5를 적용
    List<Translation> findTopBy5ByUserIdOrderByCreatedDateDesc(@Param("email") String email);


}
