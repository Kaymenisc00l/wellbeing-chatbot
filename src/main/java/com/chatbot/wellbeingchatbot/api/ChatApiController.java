package com.chatbot.wellbeingchatbot.api;

import com.chatbot.wellbeingchatbot.nlp.Emotion;
import com.chatbot.wellbeingchatbot.nlp.KeywordNLPService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatApiController {

    private final KeywordNLPService nlpService;

    public ChatApiController(KeywordNLPService nlpService) {
        this.nlpService = nlpService;
    }

    @PostMapping("/feeling")
    public EmotionResponse handleFeeling(@RequestBody EmotionRequest request) {

        Emotion emotion = nlpService.detectEmotion(request.getFeeling());

        String message;

        switch (emotion) {
            case STRESS:
                message = "It sounds like you're feeling stressed. What area is this mostly related to?";
                break;
            case ANXIETY:
                message = "It sounds like you're feeling anxious. What area is this mostly related to?";
                break;
            case SADNESS:
                message = "I’m sorry you’re feeling low. What area is this mostly related to?";
                break;
            case ANGER:
                message = "It sounds like you're feeling frustrated. What area is this mostly related to?";
                break;
            case HAPPY:
                message = "Glad you’re feeling good. Want to reflect on what’s going well?";
                break;
            case TIRED:
                message = "It sounds like you’re feeling tired. What area is this mostly related to?";
                break;
            default:
                message = "Thanks for sharing. Please type one word like: stressed, anxious, sad or tired.";
                break;
        }

        List<String> options = (emotion == Emotion.UNKNOWN)
                ? List.of("stressed", "anxious", "sad", "tired")
                : List.of("University", "Relationships", "Personal", "Work");

        return new EmotionResponse(emotion.name(), message, options);
    }


@PostMapping("/category")
public CategoryResponse handleCategory(@RequestBody CategoryRequest request) {

    String category = request.getCategory().toLowerCase();

    String message;

    switch (category) {
        case "university":
            message = "University stress is very common. I can share tips on time management, workload balance and academic support.";
            break;
        case "work":
            message = "Work-related stress can come from pressure or long hours. I can help with coping strategies and boundaries.";
            break;
        case "relationships":
            message = "Relationship challenges can affect wellbeing. I can share communication and self-care tips.";
            break;
        case "personal":
            message = "Personal challenges can feel overwhelming. I can offer grounding and wellbeing techniques.";
            break;
        default:
            message = "Thanks for sharing. I’ll provide some general wellbeing guidance.";
    }

    return new CategoryResponse(
            message,
            List.of("Show tips", "Restart chat")
    );

    }

}

