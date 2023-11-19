package com.usit.writersmaven.service;

import com.usit.writersmaven.dao.ChatDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChatService {

    @Autowired
    ChatDao dao;

    public int sendChat(Map<String,String> message){
        return dao.insertMessage(message);
    }
}
