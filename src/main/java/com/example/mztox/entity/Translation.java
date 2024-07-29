package com.example.mztox.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
/*
AuditingEntityListener는 스프링 데이터 JPA에서 제공하는 리스너로,
엔티티가 데이터베이스에 삽입되거나 업데이트될 때 생성일(@CreatedDate) 및 수정일(@LastModifiedDate)을 자동으로 설정
*/
@EntityListeners(AuditingEntityListener.class)
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String slang;

    @Column(nullable = false)
    private String standard;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Builder
    public Translation(String email, String slang, String standard){
        this.email = email;
        this.slang = slang;
        this.standard = standard;
    }




}
