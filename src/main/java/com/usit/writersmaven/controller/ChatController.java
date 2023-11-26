package com.usit.writersmaven.controller;

import com.usit.writersmaven.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @Autowired
    ChatService service;

    @GetMapping("/chat/chatpage")
    public String chatpage(){
        return "chat/chatpage";
    }
    @GetMapping("/chat/list")
    public String chatList(){
        return "/chat/list";
    }

}
