package com.example.mztox.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


//회원정보를 저장하는 entity

@Getter
@Setter // 모든 필드에 대한 setter 메서드 자동 생성
@Entity
@DynamicUpdate // 업데이트 시 변경된 필드만 반영
@DynamicInsert // 삽입 시 null이 아닌 필드만 반영
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 기능 활성화,엔티티의 생성 및 수정 시간을 자동으로 관리하기 위함
public class Members {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 100, nullable = false, columnDefinition = "VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String name;

    @Column(length = 100, nullable = false)
    private String mobile;

    @CreatedDate // 엔티티가 생성될 때 자동으로 현재 시간이 설정됩니다.
    @Column(updatable = false)
    private LocalDateTime createdDate; // 회원 가입일

    @LastModifiedDate // 엔티티가 수정될 때 자동으로 현재 시간이 설정됩니다.
    private LocalDateTime modifiedDate; // 마지막 수정일

    @Builder
    public Members(Long id, String email, String password, String name, String mobile) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.mobile = mobile;

    }


}