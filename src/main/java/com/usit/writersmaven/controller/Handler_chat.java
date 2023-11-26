package com.usit.writersmaven.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usit.writersmaven.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
@Component("chatHandler")
public class Handler_chat extends TextWebSocketHandler{

    //서버로 접속한 계정의 누적접속
    private Map<String,WebSocketSession> users = new ConcurrentHashMap<>();
    private Map<String, String> userChatRooms = new ConcurrentHashMap<>();

    @Autowired
    ChatService service;


    //종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        users.remove(session.getId());
        userChatRooms.get(session.getId());
        userChatRooms.remove(session.getId());
        System.out.println("현재 접속자 : " +users.size());
    }

    //시작
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

            users.put(session.getId(),session);
        System.out.println("session : 이거 넣어서 보내라" + session.getId());
        System.out.println("users : " +users);
        System.out.println("현재 접속자 : " +users.size());
    }

    //전송 (수정)
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("session : "+session);
        System.out.println("message : "+message);
        String payload = (String)message.getPayload();
        System.out.println("payload : "+ payload);
        Map<String, String> datamap = new ObjectMapper().readValue(payload,Map.class);

        System.out.println("datamap : " + datamap);
        String senderId = session.getId();
        //String roomId = userChatRooms.get(senderId);
        String chatRoomId = datamap.get("chatRoomId");
        System.out.println("senderId : " + senderId);
        System.out.println("chatRoomId : " + chatRoomId);

        System.out.println(users);
 //     System.out.println(datamap);
        //채팅방 들어오면 id 생성해서 userChatRoom에 넣음
        //보낼때 set에 있으면 보내
        for (WebSocketSession ws : users.values()) {
            if(Objects.equals(userChatRooms.get(ws.getId()), chatRoomId )){
                ws.sendMessage(new TextMessage(payload));
            }
        }
    }

    //예외
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("~~~~~~~~~~~~핸들러 오류 발생 : "+session.getId()+" : "+exception.getMessage());
    }
}
