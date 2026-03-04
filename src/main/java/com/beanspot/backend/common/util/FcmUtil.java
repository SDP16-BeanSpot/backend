package com.beanspot.backend.common.util;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FcmUtil {

    public void sendNotification(String targetToken, String title, String body, String targetId) {
        if(targetToken == null || targetToken.isEmpty()) {
            log.warn("FCM 토큰이 없어 알림 전송이 불가합니다.");
            return;
        }

        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putData("targetId", targetId)
                .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
                .build();

        try{
            FirebaseMessaging.getInstance().send(message);
            log.info("FCM 알림 전송 성공 : {}", targetToken);
        } catch (Exception e) {
            log.error("FCM 알림 전송 실패: {}", e.getMessage());
        }

    }
}
