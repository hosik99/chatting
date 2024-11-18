package com.example.chating2.setting;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Log
@Controller
public class ChatController {

    @Value("${broker.topic}")
    private String topicPrefix;

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    //메세지 전송
    @MessageMapping("/receive") // /app/sendMessage로 보낸 메시지를 받음
    public void sendMessage( ChatMessage message ) {
        String escapedText = HtmlUtils.htmlEscape(message.getText());   //XSS 방어
        message.setText(escapedText);

        log.info(message.toString());
        messagingTemplate.convertAndSend( topicPrefix+"/"+message.getChannelId()+"/"+message.getRoomId(),message);
    }

//    // 채팅방에 파일을 보내는 @MessageMapping
//    @MessageMapping("/sendFile/{roomId}")
//    @SendTo("/topic/{roomId}")
//    public FileMessage sendFile(@DestinationVariable String roomId, FileMessage fileMessage) {
//        // 해당 채팅방에 파일 전송
//        return fileMessage;
//    }
//
//    // 채팅방에서 알림을 보내는 @MessageMapping
//    @MessageMapping("/sendNotification/{roomId}")
//    @SendTo("/topic/{roomId}")
//    public Notification sendNotification(@DestinationVariable String roomId, Notification notification) {
//        // 해당 채팅방에 알림 전송
//        return notification;
//    }
}

