package com.beanspot.backend.service.notification;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.dto.user.NotificationDTO;
import com.beanspot.backend.entity.User;
import com.beanspot.backend.entity.notification.NotificationSetting;
import com.beanspot.backend.repository.NotificationSettingRepository;
import com.beanspot.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationSettingServiceImpl implements  NotificationSettingService {

    private final NotificationSettingRepository settingRepository;
    private final UserRepository userRepository;

    @Override
    public NotificationDTO.SettingResponse getSetting(Long userId) {
        NotificationSetting setting = settingRepository.findByUserId(userId)
                .orElseGet(() -> initDefaultSetting(userId));
        return NotificationDTO.SettingResponse.from(setting);
    }

    @Override
    @Transactional
    public NotificationDTO.SettingResponse updateNotificationSetting(Long userId, NotificationDTO.UpdateSettingRequest request) {
        NotificationSetting setting = settingRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_SETTING_NOT_FOUND));

        if(request.getOptionType() != null && !request.getOptionType().isBlank()){
            setting.updateDetailOption(request.getCategory(), request.getOptionType(), request.getEnabled());
        }else{
            setting.updateCategory(request.getCategory(), request.getEnabled());
        }

        return NotificationDTO.SettingResponse.from(setting);
    }

    private NotificationSetting initDefaultSetting(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_USERID_NOT_FOUND));
        return settingRepository.save(NotificationSetting.createDefault(user));
    }
}
