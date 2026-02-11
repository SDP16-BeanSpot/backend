package com.beanspot.backend.controller;

import com.beanspot.backend.common.response.ApiResponse;
import com.beanspot.backend.dto.user.UserProfileDTO;
import com.beanspot.backend.entity.User;
import com.beanspot.backend.security.CurrentUserId;
import com.beanspot.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name="USER 관련 API", description = "마이페이지 관련 API입니다.")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ApiResponse<?> getProfile(@CurrentUserId Long userId) {
         UserProfileDTO user = userService.getUserProfileById(userId);
         return ApiResponse.ok(user);
    }


}
