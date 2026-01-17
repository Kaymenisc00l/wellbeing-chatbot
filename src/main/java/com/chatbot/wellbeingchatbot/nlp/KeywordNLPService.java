package com.chatbot.wellbeingchatbot.nlp;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class KeywordNLPService {

    private final Map<Emotion, Set<String>> keywords = Map.of(
            Emotion.STRESS, Set.of("stressed", "stress", "overwhelmed", "pressure"),
            Emotion.ANXIETY, Set.of("anxious", "anxiety", "nervous", "worried"),
            Emotion.SADNESS, Set.of("sad", "down", "unhappy", "low"),
            Emotion.ANGER, Set.of("angry", "mad", "frustrated", "irritated"),
            Emotion.HAPPY, Set.of("happy", "good", "great", "fine"),
            Emotion.TIRED, Set.of("tired", "exhausted", "drained", "fatigued")
    );

    public Emotion detectEmotion(String input) {
        if (input == null) return Emotion.UNKNOWN;

        String cleaned = input.trim().toLowerCase();

        for (var entry : keywords.entrySet()) {
            if (entry.getValue().contains(cleaned)) {
                return entry.getKey();
            }
        }
        return Emotion.UNKNOWN;
    }
}

