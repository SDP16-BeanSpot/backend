package com.beanspot.backend.service;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.dto.announcement.AnnouncementSummaryDTO;
import com.beanspot.backend.dto.user.UserDTO;
import com.beanspot.backend.entity.Bookmark;
import com.beanspot.backend.entity.RecentAnnouncementView;
import com.beanspot.backend.entity.User;
import com.beanspot.backend.entity.announcement.Announcement;
import com.beanspot.backend.repository.BookmarkRepository;
import com.beanspot.backend.repository.RecentAnnouncementViewRepository;
import com.beanspot.backend.repository.UserRepository;
import com.beanspot.backend.repository.announcement.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MypageServiceImpl implements MypageService {

    private final UserRepository userRepository;
    private final AnnouncementRepository announcementRepository;
    private final BookmarkRepository bookmarkRepository;
    private final S3Service s3Service;
    private final RecentAnnouncementViewRepository recentAnnouncementViewRepository;

    @Override
    @Transactional
    public UserDTO.ProfileResponse updateProfile(Long userId, String nickname, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_USERID_NOT_FOUND));

        String uploadedImgUrl = null;

        if(image != null && !image.isEmpty()) {
            uploadedImgUrl = s3Service.uploadFile(image);
        }

        user.updateProfile(nickname, uploadedImgUrl);
        return UserDTO.ProfileResponse.from(user);
    }

    @Override
    public UserDTO.ProfileResponse getAccountInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_USERID_NOT_FOUND));
        return UserDTO.ProfileResponse.from(user);
    }

    @Override
    public UserDTO.ProfileResponse updateAccount(Long userId, UserDTO.UpdateAccountRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_USERID_NOT_FOUND));
        //휴대폰 번호 중복 체크
        //정보 업데이트
        return null;
    }

    @Override
    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_USERID_NOT_FOUND));

        user.withdraw();
        userRepository.delete(user);

        log.info("User Soft Deleted: {}", userId);

    }

    @Override
    @Transactional
    public boolean toggleBookmark(Long userId, Long announcementId) {
        if (!announcementRepository.existsById(announcementId)) {
            throw new CustomException(ErrorCode.ANNOUNCEMENT_NOT_FOUND);
        }

        Optional<Bookmark> bookmarkOpt =
                bookmarkRepository.findByUserIdAndAnnouncementId(userId, announcementId);

        if(bookmarkOpt.isPresent()) {
            bookmarkRepository.delete(bookmarkOpt.get());
            return false;
        }

        try {
            bookmarkRepository.save(
                    Bookmark.builder()
                            .user(User.builder().id(userId).build())
                            .announcement(Announcement.builder().id(announcementId).build())
                            .build()
            );
            return true;
        } catch (DataIntegrityViolationException e) {
            // 동시성으로 이미 등록된 경우
            return true;
        }

    }

    @Override
    public List<AnnouncementSummaryDTO> getMyBookmarks(Long userId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.AUTH_USERID_NOT_FOUND);
        }

        return bookmarkRepository.findBookmarkedAnnouncements(userId);
    }

    @Override
    @Transactional
    public void saveRecentAnnouncementView(Long userId, Long announcementId) {
        if (!announcementRepository.existsById(announcementId)) {
            throw new CustomException(ErrorCode.ANNOUNCEMENT_NOT_FOUND);
        }
        Optional<RecentAnnouncementView> existingView =
                recentAnnouncementViewRepository.findByUserIdAndAnnouncementId(userId, announcementId);
        if(existingView.isPresent()) {
            existingView.get().touch();
        }else{
            try {
                if (recentAnnouncementViewRepository.countByUserId(userId) >= 20) {
                    recentAnnouncementViewRepository.findFirstByUserIdOrderByUpdatedAtAsc(userId)
                            .ifPresent(recentAnnouncementViewRepository::delete);
                }
                recentAnnouncementViewRepository.save(
                        RecentAnnouncementView.builder()
                                .user(User.builder().id(userId).build())
                                .announcement(Announcement.builder().id(announcementId).build())
                                .build()
                );
            } catch (DataIntegrityViolationException e) {
                // 동시성으로 이미 등록된 경우
            }
        }
    }

    @Override
    public List<AnnouncementSummaryDTO> getRecentAnnouncementViews(Long userId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.AUTH_USERID_NOT_FOUND);
        }

        return recentAnnouncementViewRepository.findRecentAnnouncements(userId);
    }

}
