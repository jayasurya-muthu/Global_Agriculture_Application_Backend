package com.globalagriculture.backend.repository;

import com.globalagriculture.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    long countByUserIdAndReadFalse(Long userId);
}
