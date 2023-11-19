package com.usit.writersmaven;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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



    //종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        users.remove(session.getId());
    }

    //시작
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        users.put(session.getId(),session);
        System.out.println("현재 접속자 : " +users.size());
    }

    //전송 (수정)
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String revMsg = (String)message.getPayload();
        System.out.println("메세지 내용: " + revMsg);

        for (WebSocketSession ws : users.values()) {
            ws.sendMessage(new TextMessage(revMsg));
        }
    }

    //예외
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("~~~~~~~~~~~~핸들러 오류 발생 : "+session.getId()+" : "+exception.getMessage());
    }
}
