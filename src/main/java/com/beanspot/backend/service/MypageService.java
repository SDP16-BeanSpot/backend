package com.beanspot.backend.service;

import com.beanspot.backend.dto.announcement.AnnouncementSummaryDTO;
import com.beanspot.backend.dto.user.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MypageService {
    UserDTO.ProfileResponse updateProfile(Long userId, String nickname, MultipartFile image);
    UserDTO.ProfileResponse getAccountInfo(Long userId);
    UserDTO.ProfileResponse updateAccount(Long userId, UserDTO.UpdateAccountRequest request);
    void withdraw(Long userId);
    boolean toggleBookmark(Long userId, Long announcementId);
    List<AnnouncementSummaryDTO> getMyBookmarks(Long userId);
    void saveRecentAnnouncementView(Long userId, Long announcementId);
    List<AnnouncementSummaryDTO> getRecentAnnouncementViews(Long userId);
}
