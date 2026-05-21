package com.beanspot.backend.repository;

import com.beanspot.backend.entity.notification.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

    //유저의 모든 기기 토큰 조회
    List<UserDevice> findAllByUserId(Long userDeviceId);

    //특정 기기 조회 (토큰 갱신 시 사용)
    Optional<UserDevice> findByUserIdAndDeviceId(Long userId, String deviceId);

    void deleteByUserIdAndDeviceId(Long userId, String deviceId);

}
