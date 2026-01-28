package com.chatbot.wellbeingchatbot.guidance;
import java.util.List;


public class GuidanceContent {

    private String message;
    private List<String> tips;
    private List<String> links;

    public GuidanceContent(String message, List<String> tips, List<String> links){
        this.message = message;
        this.tips = tips;
        this.links = links;
    }

    public String getMessage(){
        return message;
    }

    public List<String> getTips(){
        return tips;
    }

    public List<String> getLinks(){
        return links;
    }

    
}
