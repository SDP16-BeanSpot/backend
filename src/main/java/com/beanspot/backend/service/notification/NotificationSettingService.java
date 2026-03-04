package com.beanspot.backend.service.notification;

import com.beanspot.backend.dto.user.NotificationDTO;

public interface NotificationSettingService {

    NotificationDTO.SettingResponse getSetting(Long userId);

    NotificationDTO.SettingResponse updateNotificationSetting(Long userId, NotificationDTO.UpdateSettingRequest setting);

}
