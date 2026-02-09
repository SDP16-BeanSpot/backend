package com.beanspot.backend.service;

import com.beanspot.backend.dto.auth.LoginUserDTO;
import com.beanspot.backend.dto.auth.SignUpSocialUserDTO;
import com.beanspot.backend.dto.auth.SignUpUserDTO;
import com.beanspot.backend.dto.user.UserProfileDTO;
import com.beanspot.backend.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
    SignUpUserDTO.Res createUser(final SignUpUserDTO.Req userDTO);
    User getByCredential(final String email, final String password, PasswordEncoder encoder);
    LoginUserDTO.Res login(LoginUserDTO.Req userDTO, PasswordEncoder encoder);
    boolean isNicknameAvailable(final String nickname);
    boolean isUserIdAvailable(final String userId);
    UserProfileDTO getUserProfileById(final Long id);
    SignUpSocialUserDTO.Res createSocialUser(final String socialId, final SignUpSocialUserDTO.Req userDTO);
    LoginUserDTO.Res loginBySocialId(final String socialType, final String socialId);
}
