package com.usit.writersmaven.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class Handler_chat extends TextWebSocketHandler {

    //private static Map<String, WebSocketSession> sessions = new HashMap<>();
    private static Map<String, Map<String, WebSocketSession>> chatRooms = new HashMap<>();  //채팅방 아이디, 세션들맵
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // Add the user session to the global session map
       // sessions.put(session.getId(), session);

        // Notify the user about joining the chat room
        Map<String, String> joinMessage = new HashMap<>();
        joinMessage.put("type", "SYSTEM");
        joinMessage.put("session", session.getId() );
        joinMessage.put("content", chatRooms.toString() );
        System.out.println("chatRooms  toString" + chatRooms.toString());
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(joinMessage)));
    }

    public void createChatRoom(WebSocketSession session) throws Exception{
        Map<String, WebSocketSession> members = new HashMap<>();
        members.put(session.getId(), session);
        // Create a unique chat room ID for the user
        String chatRoomId = UUID.randomUUID().toString();
        System.out.println("chatRoomId Is "+chatRoomId);
        // Map the user session to the chat room ID
        chatRooms.put(chatRoomId, members);
        Map<String, String> joinMessage = new HashMap<>();
        joinMessage.put("type", "SYSTEM");
        joinMessage.put("chatRoomId", chatRoomId);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(joinMessage)));
    }
    public void joinChatRoom(WebSocketSession session,String chatRoomId){
        chatRooms.get(chatRoomId).put(session.getId(),session);
        System.out.println(chatRooms);
    }
    @Override
    protected void handleTextMessage(WebSocketSession senderSession, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String, String> messageData = objectMapper.readValue(payload, Map.class);
        messageData.put("session",senderSession.getId());
        System.out.println(messageData);
        String messageType = messageData.get("type");
        String chatRoomId = messageData.get("chatRoomId");
        if ("CREATE".equals(messageType)) {
            createChatRoom(senderSession);
        }
        if ("JOIN".equals(messageType)) {
            joinChatRoom(senderSession,chatRoomId);
        }
        if ("MESSAGE".equals(messageType)) {
            if (chatRoomId != null) {
                sendToChatRoom(senderSession, chatRoomId, messageData);
            } else {
                senderSession.sendMessage(new TextMessage("You are not in a chat room."));
            }
        }
    }

    private void sendToChatRoom(WebSocketSession senderSession, String chatRoomId, Map<String, String> message) throws Exception {
        // Get all sessions in the chat room
        Map<String, WebSocketSession> chatRoomSessions = chatRooms.get(chatRoomId);
        System.out.println("send to chat room");
        if (chatRoomSessions != null) {
            for (WebSocketSession recipient : chatRoomSessions.values()) {
                try {
                    if (recipient.isOpen()) {
                        recipient.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Handle the case where the chat room is not found
            senderSession.sendMessage(new TextMessage("Chat room not found: " + chatRoomId));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove the user session from the global session map
        //sessions.remove(session.getId());

        // Remove the user session from the chat room mapping
        chatRooms.values().forEach(chatRoomSessions -> chatRoomSessions.remove(session.getId()));
    }
}