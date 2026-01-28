package com.chatbot.wellbeingchatbot.guidance;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GuidanceService {

    private final Map<String, GuidanceContent> guidanceMap = Map.of(

        "university", new GuidanceContent(
            "University stress is very common, especially during busy periods.",
            List.of(
                "Break tasks into small, manageable steps",
                "Create a realistic study schedule",
                "Take short breaks to avoid burnout"
            ),
            List.of(
                "https://www.nhs.uk/mental-health/",
                "https://www.mind.org.uk/information-support/tips-for-everyday-living/student-life/"
            )
        ),

        "work", new GuidanceContent(
            "Work-related stress can affect both mental and physical wellbeing.",
            List.of(
                "Take regular breaks during the day",
                "Set boundaries between work and personal time",
                "Speak to a manager or colleague if you feel overwhelmed"
            ),
            List.of(
                "https://www.nhs.uk/mental-health/",
                "https://www.mind.org.uk/workplace/"
            )
        ),

        "relationships", new GuidanceContent(
            "Relationship difficulties can strongly impact emotions.",
            List.of(
                "Communicate openly about how you feel",
                "Give yourself space when emotions are intense",
                "Seek support from trusted friends"
            ),
            List.of(
                "https://www.relate.org.uk/",
                "https://www.mind.org.uk/information-support/relationships/"
            )
        ),

        "personal", new GuidanceContent(
            "Personal stress can come from many different areas of life.",
            List.of(
                "Maintain a regular sleep routine",
                "Practice relaxation or breathing exercises",
                "Make time for activities you enjoy"
            ),
            List.of(
                "https://www.nhs.uk/mental-health/",
                "https://www.mind.org.uk/"
            )
        )
    );

    public GuidanceContent getGuidance(String category) {
        if (category == null) return null;
        return guidanceMap.get(category.toLowerCase());
    }
}
