package com.beanspot.backend.security;

import com.beanspot.backend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserPrincipal implements UserDetails, OAuth2User {
    private final User user;
    private Map<String, Object> attributes; // OAuth 제공자로 부터 받은 회원 정보
    private boolean oauth = false;

    public UserPrincipal(User user) {
        this.user = user;
    }

    public UserPrincipal(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
        this.oauth = true;
    }

    public static UserPrincipal create(User user) {
        return new UserPrincipal(
                User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .password(user.getPassword())
                        .nickname(user.getNickname())
                        .userId(user.getUserId())
                        .phone(user.getPhone())
                        .socialId(user.getSocialId())
                        .role(user.getRole())
                        .build()
        );
    }

    public static UserPrincipal from(User user) {
        return new UserPrincipal(user);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    public Long getId(){
        return user.getId();
    }

    public String getNickname(){
        return user.getNickname();
    }

    public String getUserId(){ return user.getUserId(); }

    public String getSocialId(){ return user.getSocialId(); }

    public String getName(){ return user.getName(); }
}

