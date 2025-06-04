package com.vueart.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vueart.api.core.enums.Code;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // JPA에서 lazy관련 에러 날 경우 사용
@Entity  // 객체와 테이블 매핑
@Table(name = "users")
public class User extends BaseEntity {
    @Id  // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT 설정 (id값이 null일 경우 자동 생성)
    @Column(name = "id")  // 컬럼 지정
    private Long id;

    @Column(name = "user_id", unique = true)
    private String userId;

    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "business", length = 1)
    @Enumerated(EnumType.STRING)
    private Code.YN business;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "region")
    private String region;

    @Enumerated(EnumType.STRING)
    private Code.Role role;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    public User updatePassword(Long id, String password) {
        return User.builder()
                .id(id)
                .email(this.email)
                .userId(this.userId)
                .password(password)
                .build();
    }
}
