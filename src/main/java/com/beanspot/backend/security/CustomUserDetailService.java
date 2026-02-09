package com.beanspot.backend.security;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.entity.User;
import com.beanspot.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    // username => email 로그인
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_EMAIL_NOT_FOUND));
        return UserPrincipal.from(user);
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.AUTH_INVALID_CREDENTIALS));
        return UserPrincipal.from(user);
    }
    public UserDetails loadUserBySocicalId(String socicalId) {
        User user = User.builder()
                .socialId(socicalId)
                .build();

        return UserPrincipal.from(user);
    }
}
