package com.usit.writersmaven.vo;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;

public class UserChatRoom {
    String chatRoomId;
    Set<WebSocketSession> sessions;

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Set<WebSocketSession> getSessions() {
        return sessions;
    }

    public void setSessions(Set<WebSocketSession> sessions) {
        this.sessions = sessions;
    }
}
