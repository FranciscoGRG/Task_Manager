package com.task_manager.notification_service.email;

public interface EmailSender {
    boolean send(String to, String subject, String body);
}
