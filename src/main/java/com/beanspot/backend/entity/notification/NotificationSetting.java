package com.beanspot.backend.entity.notification;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.entity.BaseEntity;
import com.beanspot.backend.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "notification_setting")
public class NotificationSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @JoinColumn(name = "is_bookmark_enabled")
    private boolean isBookmarkEnabled= false;      // 관심 공고 등록 알림

    @Builder.Default
    @JoinColumn(name = "is_deadline_enabled")
    private boolean isDeadlineEnabled= false;     // 공고 마감 알림

    @Builder.Default
    @JoinColumn(name = "is_activity_enabled")
    private boolean isActivityEnabled= false;     // 활동 시작 알림

    @Builder.Default
    @JoinColumn(name = "is_todo_enabled")
    private boolean isTodoEnabled= false;          // TO-DO List 알림

    @Builder.Default
    @JoinColumn(name = "is_keyword_enabled")
    private boolean isKeywordEnabled= false;      // 키워드 알림

    // 마감 알림 상세 옵션
    @Builder.Default
    @JoinColumn(name = "deadline_d7")
    private boolean deadlineD7= false;

    @Builder.Default
    @JoinColumn(name = "deadline_d3")
    private boolean deadlineD3= false;

    @Builder.Default
    @JoinColumn(name = "deadline_d1")
    private boolean deadlineD1= false;

    @Builder.Default
    @JoinColumn(name = "deadline_day")
    private boolean deadlineDay= false;

    // 활동 시작 알림 상세 옵션
    @Builder.Default
    @JoinColumn(name = "activity_d7")
    private boolean activityD7= false;

    @Builder.Default
    @JoinColumn(name = "activity_d3")
    private boolean activityD3= false;

    @Builder.Default
    @JoinColumn(name = "activity_d1")
    private boolean activityD1= false;

    @Builder.Default
    @JoinColumn(name = "activity_day")
    private boolean activityDay= false;

    public static NotificationSetting createDefault(User user) {
        return NotificationSetting.builder()
                .user(user)
                .build();
    }
    public void updateCategory(NotificationType category, boolean enabled) {
        switch (category) {
            case BOOKMARK -> this.isBookmarkEnabled = enabled;
            case DEADLINE -> this.isDeadlineEnabled = enabled;
            case ACTIVITY -> this.isActivityEnabled = enabled;
            case TODO -> this.isTodoEnabled = enabled;
            case KEYWORD -> this.isKeywordEnabled = enabled;
            default -> throw new CustomException(ErrorCode.INVALID_CATEGORY_TYPE);
        }
    }

    public void updateDetailOption(NotificationType category, String optionType, boolean enabled) {
        if (category == NotificationType.DEADLINE) {
            updateDeadlineOption(optionType, enabled);
        } else if (category == NotificationType.ACTIVITY) {
            updateActivityOption(optionType, enabled);
        } else {
            throw new CustomException(ErrorCode.INVALID_CATEGORY_TYPE);
        }
    }

    private void updateDeadlineOption(String optionType, boolean enabled) {
        switch (optionType.toUpperCase()) {
            case "D7" -> this.deadlineD7 = enabled;
            case "D3" -> this.deadlineD3 = enabled;
            case "D1" -> this.deadlineD1 = enabled;
            case "DAY" -> this.deadlineDay = enabled;
            default -> throw new CustomException(ErrorCode.INVALID_OPTION_TYPE);
        }
    }

    private void updateActivityOption(String optionType, boolean enabled) {
        switch (optionType.toUpperCase()) {
            case "D7" -> this.activityD7 = enabled;
            case "D3" -> this.activityD3 = enabled;
            case "D1" -> this.activityD1 = enabled;
            case "DAY" -> this.activityDay = enabled;
            default -> throw new CustomException(ErrorCode.INVALID_OPTION_TYPE);
        }
    }
}
