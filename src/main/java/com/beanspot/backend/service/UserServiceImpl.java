package com.beanspot.backend.service;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.dto.auth.LoginUserDTO;
import com.beanspot.backend.dto.auth.SignUpSocialUserDTO;
import com.beanspot.backend.dto.auth.SignUpUserDTO;
import com.beanspot.backend.dto.user.UserProfileDTO;
import com.beanspot.backend.entity.Role;
import com.beanspot.backend.entity.SocialType;
import com.beanspot.backend.entity.User;
import com.beanspot.backend.repository.UserRepository;
import com.beanspot.backend.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    public SignUpUserDTO.Res createUser(final SignUpUserDTO.Req userDTO) {

        final String userId = userDTO.getUserId();
        final String nickname = userDTO.getNickname();

        if(userRepository.existsByUserId(userId)){
            log.warn("User with userId {} already exists", userId);
            throw new CustomException(ErrorCode.AUTH_USERID_ALREADY_EXISTS);
        }

        if(!isNicknameAvailable(nickname)){
            log.warn("User with nickname {} already exists", nickname);
            throw new CustomException(ErrorCode.AUTH_NICKNAME_ALREADY_EXISTS);
        }

        User user = User.builder()
                .userId(userDTO.getUserId())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .nickname(userDTO.getNickname())
                .name(userDTO.getName())
                .socialType(SocialType.NONE)
                .role(userDTO.getRole() == null ? Role.USER : userDTO.getRole())
                .build();

        userRepository.save(user);

        return  SignUpUserDTO.Res.builder()
                .userId(userDTO.getUserId())
                .nickname(user.getNickname())
                .build();

    }

    public User getByCredential(final String userId, final String password, PasswordEncoder encoder) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.AUTH_EMAIL_NOT_FOUND));
        if(!encoder.matches(password, user.getPassword())){
            throw new CustomException(ErrorCode.AUTH_PASSWORD_MISMATCH);
        }
        return user;
    }

    public LoginUserDTO.Res login(LoginUserDTO.Req userDTO, PasswordEncoder encoder) {
        User user = userRepository.findByUserId(userDTO.getUserId())
                .orElseThrow(()-> new CustomException(ErrorCode.AUTH_EMAIL_NOT_FOUND));
        if(!encoder.matches(userDTO.getPassword(), user.getPassword())){
            throw new CustomException(ErrorCode.AUTH_PASSWORD_MISMATCH);
        }
        final String accessToken = tokenProvider.createAccessToken(user);

        LoginUserDTO.Res reponseUserDTO = LoginUserDTO.Res.builder()
                .accessToken(accessToken)
                .nickname(user.getNickname())
                .id(user.getId())
                .role(user.getRole().name())
                .build();
        return reponseUserDTO;
    }

    public boolean isNicknameAvailable(final String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Override
    public boolean isUserIdAvailable(String userId) {
        return !userRepository.existsByUserId(userId);
    }

    @Override
    public UserProfileDTO getUserProfileById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.AUTH_LOGIN_REQUIRED));

        return UserProfileDTO.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .name(user.getName())
                .socialId(user.getSocialId())
                .socialType(user.getSocialType())
                .build();
    }
    public LoginUserDTO.Res loginBySocialId(String socialType, String socialId) {
        Optional<User> user = userRepository.findBySocialId(socialId);

        if(user.isPresent()){
            String accessToken = tokenProvider.createAccessToken(user.get());
            return LoginUserDTO.Res.builder()
                    .accessToken(accessToken)
                    .nickname(user.get().getNickname())
                    .id(user.get().getId())
                    .role(user.get().getRole().name())
                    .isProfileComplete(true)
                    .build();
        }else{
            String tempToken = tokenProvider.createTemporaryAccessToken(socialType, socialId);
            return LoginUserDTO.Res.builder()
                    .accessToken(tempToken)
                    .isProfileComplete(false)
                    .build();
        }

    }

    //소셜 회원 추가 정보 입력 절차
    public SignUpSocialUserDTO.Res createSocialUser(String socialId, SignUpSocialUserDTO.Req userDTO) {
        final String nickname = userDTO.getNickname();

        if(userRepository.existsByUserId(socialId)){
            log.warn("User with userId {} already exists", socialId);
            throw new CustomException(ErrorCode.AUTH_SOCIAL_ID_ALREADY_EXISTS);
        }

        if(!isNicknameAvailable(nickname)){
            log.warn("User with nickname {} already exists", nickname);
            throw new CustomException(ErrorCode.AUTH_NICKNAME_ALREADY_EXISTS);
        }

        User user = User.builder()
                .nickname(userDTO.getNickname())
                .name(userDTO.getUserName())
                .socialType(SocialType.KAKAO)
                .socialId(socialId)
                .build();

        userRepository.save(user);

        return  SignUpSocialUserDTO.Res.builder()
                .socialId(user.getSocialId())
                .nickname(user.getNickname())
                .build();
    }

}
