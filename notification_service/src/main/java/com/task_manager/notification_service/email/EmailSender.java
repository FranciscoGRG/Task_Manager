package com.task_manager.notification_service.email;

import org.springframework.stereotype.Component;

public interface EmailSender {
    boolean send(String to, String subject, String body);
}
