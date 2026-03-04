package com.beanspot.backend.dto.user;

import com.beanspot.backend.entity.SocialType;
import com.beanspot.backend.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDTO {

    // 프로필 수정 요청
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateProfileRequest {
        private String nickname;
        private String profileUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ProfileResponse {
        private Long id;
        private String userId;
        private String nickname;
        private String profileUrl;
        private String name;
        private String phone;
        private SocialType socialType;

        public static ProfileResponse from(User user) {
            return ProfileResponse.builder()
                    .id(user.getId())
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .profileUrl(user.getProfileUrl())
                    .name(user.getName())
                    .phone(user.getPhone())
                    .socialType(user.getSocialType())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Summary {
        private Long id;
        private String nickname;
        private String profileUrl;

        public static Summary from(User user) {
            return Summary.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileUrl(user.getProfileUrl())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAccountRequest {
        @NotBlank(message = "연락처는 필수입니다.")
        private String phone;
    }
}
