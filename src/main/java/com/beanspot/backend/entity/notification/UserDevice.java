package com.beanspot.backend.entity.notification;

import com.beanspot.backend.entity.BaseEntity;
import com.beanspot.backend.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user_device")
public class UserDevice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String deviceId; // 기기 고유 식별자

    @Column(nullable = false)
    private String fcmToken;

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

}
