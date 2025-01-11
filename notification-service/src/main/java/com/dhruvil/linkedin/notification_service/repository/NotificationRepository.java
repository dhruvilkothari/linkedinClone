package com.dhruvil.linkedin.notification_service.repository;


import com.dhruvil.linkedin.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
