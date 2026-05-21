package com.beanspot.backend.dto.user;

import com.beanspot.backend.entity.notification.NotificationHistory;
import com.beanspot.backend.entity.notification.NotificationSetting;
import com.beanspot.backend.entity.notification.NotificationType;
import com.beanspot.backend.entity.notification.UserKeyword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NotificationDTO {

    @Getter
    @NoArgsConstructor
    public static class UpdateSettingRequest {
        @NotBlank
        private NotificationType category;   // bookmark, deadline, activity, todo, keyword

        private String optionType; // D7, D3, D1, DAY (null 허용)

        @NotNull
        private Boolean enabled;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SettingResponse {
        private Long id;
        private String userId;
        private boolean isBookmarkEnabled;
        private boolean isDeadlineEnabled;
        private boolean isActivityEnabled;
        private boolean isTodoEnabled;
        private boolean isKeywordEnabled;
        private boolean deadlineD7;
        private boolean deadlineD3;
        private boolean deadlineD1;
        private boolean deadlineDay;
        private boolean activityD7;
        private boolean activityD3;
        private boolean activityD1;
        private boolean activityDay;

        public static NotificationDTO.SettingResponse from(NotificationSetting setting) {
            return SettingResponse.builder()
                    .id(setting.getId())
                    .userId(setting.getUser().getUserId())
                    .isBookmarkEnabled(setting.isBookmarkEnabled())
                    .isDeadlineEnabled(setting.isDeadlineEnabled())
                    .isActivityEnabled(setting.isActivityEnabled())
                    .isTodoEnabled(setting.isTodoEnabled())
                    .isKeywordEnabled(setting.isKeywordEnabled())
                    .deadlineD7(setting.isDeadlineD7())
                    .deadlineD3(setting.isDeadlineD3())
                    .deadlineD1(setting.isDeadlineD1())
                    .deadlineDay(setting.isDeadlineDay())
                    .activityD7(setting.isActivityD7())
                    .activityD3(setting.isActivityD3())
                    .activityD1(setting.isActivityD1())
                    .activityDay(setting.isActivityDay())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeywordRequest {
        @NotBlank(message = "키워드를 입력해주세요.")
        @Size(max = 20, message = "키워드는 20자 이내로 입력해주세요.")
        private String keyword;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class KeywordResponse {
        private Long id;          // 삭제 시 필요한 고유 ID
        private String keyword;   // 화면에 표시할 키워드명


        public static KeywordResponse from(UserKeyword userKeyword) {
            return KeywordResponse.builder()
                    .id(userKeyword.getId())
                    .keyword(userKeyword.getKeyword())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class HistoryResponse {
        private Long id;
        private String title;
        private String content;
        private NotificationType type;
        private boolean isRead;
        private LocalDateTime createdAt;

        public static HistoryResponse from(NotificationHistory history) {
            return HistoryResponse.builder()
                    .id(history.getId())
                    .title(history.getTitle())
                    .content(history.getContent())
                    .type(history.getType())
                    .isRead(history.isRead())
                    .createdAt(history.getCreatedAt())
                    .build();
        }
    }
}
