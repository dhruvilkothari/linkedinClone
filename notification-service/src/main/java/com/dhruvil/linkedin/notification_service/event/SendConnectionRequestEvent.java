package com.dhruvil.linkedin.notification_service.event;

import lombok.Builder;
import lombok.Data;

@Data
public class SendConnectionRequestEvent {
    private Long senderId;
    private Long receiverId;
}