package com.example.coco_spring.websocketproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WSService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    @Autowired
    public WSService(SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    public void notifyFrontend(final String message) {
        ResponseMessage response = new ResponseMessage(message);
        notificationService.sendGlobalNotification();

        messagingTemplate.convertAndSend("/chat/messages", response);
    }

    public void notifyUser(final String id, final String message) {
        ResponseMessage response = new ResponseMessage(message);
        response.setContent(message);
        ChatMessage c =new ChatMessage() ;
        c.setText(message);
        notificationService.sendPrivateNotification(id);
        messagingTemplate.convertAndSendToUser(id, "/chat/private-messages", c);
    }
}
