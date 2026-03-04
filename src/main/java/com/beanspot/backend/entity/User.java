package com.beanspot.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SoftDelete(columnName = "is_deleted")
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "nickname"),
        @UniqueConstraint(columnNames = "user_id")
})
public class User extends BaseEntity { // BaseEntity 상속으로 생성/수정 시간 관리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId; // 사용자 로그인 아이디

    @Column(name = "password", length = 100)
    private String password; // 암호화된 비밀번호

    private String name; // 사용자 실명

    @Column(unique = true, nullable = false, length = 15)
    private String nickname; // 서비스 활동 닉네임

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType; // KAKAO, NAVER 등

    @Column(name = "social_id", unique = true)
    private String socialId; // 소셜 서비스 고유 식별값

    @Column(name = "profile_url")
    private String profileUrl; // 프로필 이미지 경로

    @Column(length = 20, unique = true)
    private String phone; // 연락처

    private String address; // 주소 정보


    @Column(name = "refresh_token")
    private String refreshToken; // JWT 갱신용 토큰

    @Builder.Default
    @Column(nullable = false)
    private boolean emailVerified = false; // 이메일 인증 여부

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // USER 혹은 ADMIN


    /**
     * 💡 핵심 수정 사항: 데이터 저장 전 기본값 할당
     * @Builder 사용 시 필드 초기값이 null이 되는 문제를 방지합니다.
     */

    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = Role.USER; // 타입을 Role로 통일!
        }
    }

    /**
     * 프로필 정보 업데이트 (닉네임 및 이미지)
     */
    public void updateProfile(String nickname, String profileUrl) {
        if (nickname != null && !nickname.isBlank()) {
            this.nickname = nickname;
        }
        if (profileUrl != null) {
            this.profileUrl = profileUrl;
        }
    }

    public void withdraw(){
        this.nickname = "deleted_" + UUID.randomUUID().toString().substring(0, 8) + "_" + this.nickname ;
    }
}