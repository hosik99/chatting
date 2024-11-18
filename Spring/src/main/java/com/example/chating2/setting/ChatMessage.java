package com.example.chating2.setting;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessage {

    private String username;
    private String text;

    private Long roomId;
    private Long channelId;

    public ChatMessage() {}

    public ChatMessage(String username, String text, Long roomId, Long channelId) {
        this.username = username;
        this.text = text;
        this.roomId = roomId;
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "username='" + username + '\'' +
                ", text='" + text + '\'' +
                ", roomId=" + roomId +
                ", channelId=" + channelId +
                '}';
    }
}
