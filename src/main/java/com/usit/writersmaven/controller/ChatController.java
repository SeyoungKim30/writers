package com.usit.writersmaven.controller;

import com.usit.writersmaven.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class ChatController {

    @Autowired
    ChatService service;

    public String SendChat(Map<String,String> message){
        return service.sendChat(message)+"";
    }

}
