package com.usit.writersmaven.controller;

import com.usit.writersmaven.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.inject.Inject;

@Controller
public class HomeController {

    @Autowired
    HomeService service;

    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/contactlist")
    public String contactlist(){
        return "contactlist";
    }

    @GetMapping("/seyoung")
    public String seyoung(Model model){
        model.addAttribute("get",service.seyoung());
        return "contactlist";
    }

}
