package com.usit.writersmaven.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class HomeService {

    @GetMapping("seyoung")
    public String seyoung(){
        return "seyoung kim";
    }
}
