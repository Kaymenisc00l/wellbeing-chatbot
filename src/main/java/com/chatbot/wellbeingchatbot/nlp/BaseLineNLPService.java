package com.chatbot.wellbeingchatbot.nlp;

import org.springframework.stereotype.Service;

@Service
public class BaseLineNLPService{

    private final KeywordNLPService keywordNLPService;

    public BaseLineNLPService(KeywordNLPService keywordNLPService){
        this.keywordNLPService = keywordNLPService;
    }

    public Emotion detectEmotion(String input){
        return keywordNLPService.detectEmotion(input);
    }

}
