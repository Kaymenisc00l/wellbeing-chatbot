package com.chatbot.wellbeingchatbot.api;

import com.chatbot.wellbeingchatbot.guidance.GuidanceService;
import com.chatbot.wellbeingchatbot.nlp.Emotion;
import com.chatbot.wellbeingchatbot.nlp.KeywordNLPService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatApiController {

    private final KeywordNLPService nlpService;

    private final GuidanceService guidanceService;
    

    public ChatApiController(KeywordNLPService nlpService, GuidanceService guidanceService) {
        this.nlpService = nlpService;
        this.guidanceService = guidanceService;
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
    List<String> tips;
    List<String> links;

    switch (category) {

        case "university":
            message = "University stress is very common.";
            tips = List.of(
                "Break assignments into smaller tasks",
                "Create a weekly study plan",
                "Take short breaks between sessions"
            );
            links = List.of(
                "https://www.nhs.uk/mental-health/",
                "https://www.mind.org.uk/information-support/tips-for-everyday-living/student-life/"
            );
            break;

        case "work":
            message = "Work-related stress can build up over time.";
            tips = List.of(
                "Take regular breaks",
                "Set boundaries outside working hours",
                "Speak to someone you trust"
            );
            links = List.of(
                "https://www.mind.org.uk/workplace/"
            );
            break;

        case "relationships":
            message = "Relationship challenges can be emotionally draining.";
            tips = List.of(
                "Communicate openly",
                "Take time to cool off during conflict",
                "Seek support from friends"
            );
            links = List.of(
                "https://www.relate.org.uk/"
            );
            break;

        case "personal":
            message = "Personal difficulties can feel overwhelming.";
            tips = List.of(
                "Maintain a regular sleep routine",
                "Practice breathing exercises",
                "Spend time doing something enjoyable"
            );
            links = List.of(
                "https://www.mind.org.uk/"
            );
            break;

        default:
            message = "Thanks for sharing.";
            tips = List.of("Try taking a short break and focusing on your breathing.");
            links = List.of("https://www.nhs.uk/mental-health/");
    }

    return new CategoryResponse(
            message,
            List.of("Restart chat"),
            tips,
            links
    );
}
    

}

