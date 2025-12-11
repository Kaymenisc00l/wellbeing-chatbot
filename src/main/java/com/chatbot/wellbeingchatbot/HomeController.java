package com.chatbot.wellbeingchatbot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {

    public String home(){
        return "index";
    }
    
}
