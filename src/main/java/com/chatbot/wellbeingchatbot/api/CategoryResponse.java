package com.chatbot.wellbeingchatbot.api;
import java.util.List;

public class CategoryResponse {

    private String message;
    private List<String> nextOptions;
    private List<String> tips;
    private List<String> links;

    public CategoryResponse(
        String message,
        List<String> nextOptions,
        List<String> tips,
        List<String> links
    ) {
        this.message = message;
        this.nextOptions = nextOptions;
        this.tips = tips;
        this.links = links;
    }

    public String getMessage(){
        return message;
    }

    public List<String> getNextOptions(){
        return nextOptions;
    }

    public List<String> getTips(){
        return tips;
    }

    public List<String> getLinks(){
        return links;

    
}

}


