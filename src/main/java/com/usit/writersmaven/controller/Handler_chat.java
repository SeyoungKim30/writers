package com.usit.writersmaven.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Component
public class Handler_chat extends TextWebSocketHandler {

    private static Map<String, Set<WebSocketSession> > chatRooms = new HashMap<>();  //채팅방 아이디, 세션들맵
    private static ObjectMapper objectMapper = new ObjectMapper();


    //setNewMessage(type,chatRoomId,session,sender,content)
    public Map<String, String> setNewMessage(String type,String chatRoomId,String session,String sender,String content){
        Map<String, String> newMessage = new HashMap<>();
        if(type!=null)          newMessage.put("type",type);
        if(chatRoomId!=null)    newMessage.put("chatRoomId",chatRoomId);
        if(session!=null)       newMessage.put("session",session);
        if(sender!=null)        newMessage.put("sender",sender);
        if(content!=null)       newMessage.put("content",content);
        return newMessage;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("새 연결" + session.getId());
        System.out.println("chatRooms toString" + chatRooms.toString());
        //회원 데이터베이스의 세션 변경하는 작업

        String content = chatRooms.keySet().toString();
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(setNewMessage("OPEN",null,session.getId(),null,content))));
    }

    public void createChatRoom(WebSocketSession session) throws Exception{
        Set<WebSocketSession> members = new HashSet<>();
        members.add(session);
        // Create a unique chat room ID for the user
        String chatRoomId = UUID.randomUUID().toString();
        System.out.println("chatRoomId Is "+chatRoomId);
        // Map the user session to the chat room ID
        chatRooms.put(chatRoomId, members);
        System.out.println("chatrooms 들어갔는지 확인"+chatRooms);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString( setNewMessage("CREATE",chatRoomId,session.getId(),null,null))));
    }

    public void joinChatRoom(WebSocketSession session,String chatRoomId) throws Exception{
        chatRooms.get(chatRoomId).add(session);
        System.out.println(chatRooms);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString( setNewMessage("JOIN",chatRoomId,session.getId(),null,null))));
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
        Set<WebSocketSession> chatRoomSessions = chatRooms.get(chatRoomId);
        System.out.println("send to chat room : chatroomsessions set = "+chatRoomSessions);
        if (chatRoomSessions != null) {
            for (WebSocketSession recipient : chatRoomSessions) {
                System.out.println("방찾으러 꺼내진 세션"+recipient);
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