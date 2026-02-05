package com.beanspot.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "nickname")})
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId; // 사용자 아이디

    @Column(name = "password", length = 100)
    private String password; // 비밀번호

    private String name; // 본명

    @Column(unique = true, nullable = false, length = 15)
    private String nickname; // 닉네임


    @Enumerated(EnumType.STRING)
    @Column(name = "social_type")
    private SocialType socialType; // 카카오, 네이버 등

    @Column(name = "social_id", unique = true)
    private String socialId;


    @Column(name = "profile_url")
    private String profileUrl; // 프로필 이미지 경로

    @Column(length = 20, unique = true)
    private String phone; // 휴대번호

    private String address; // 주소

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER; // USER 혹은 ADMIN 저장

    @Column(name = "refresh_token")
    private String refreshToken;

    private boolean emailVerified;

}
