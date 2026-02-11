package com.beanspot.backend.dto.auth;

import com.beanspot.backend.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class SignUpUserDTO {

    @Getter
    public static class Req {
        @NotBlank(message = "아이디 입력은 필수입니다.")
        private String userId;

        @NotBlank(message = "비밀번호 입력은 필수입니다.")
        private String password;

        @NotBlank(message = "닉네임 작성은 필수입니다.")
        private String nickname;

        @NotBlank(message = "사용자 이름 작성은 필수입니다.")
        private String name;

        private String phone;

        private Role role;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Res {
        private String userId;
        private String nickname;
        private boolean emailVerified;
    }
}
