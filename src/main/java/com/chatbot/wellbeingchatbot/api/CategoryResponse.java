package com.chatbot.wellbeingchatbot.api;
import java.util.List;

public class CategoryResponse {

    private String message;
    private List<String> nextOptions;

    public CategoryResponse(String message, List<String> nextOptions){
        this.message = message;
        this.nextOptions = nextOptions;

    }

    public String getMessage(){
        return message;
    }

    public List<String> getNextOptions(){
        return nextOptions;
    }

    
}
