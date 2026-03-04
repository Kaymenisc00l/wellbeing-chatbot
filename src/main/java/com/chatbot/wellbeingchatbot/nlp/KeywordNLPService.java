package com.chatbot.wellbeingchatbot.nlp;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class KeywordNLPService {

    private static final Pattern NON_LETTER = Pattern.compile("[^a-z\\s]");


    private final Map<Emotion, Set<String>> keywords = Map.of(

        
            Emotion.STRESS, Set.of("stressed", "stress", "overwhelmed", "pressure"),
            Emotion.ANXIETY, Set.of("anxious", "anxiety", "nervous", "worried"),
            Emotion.SADNESS, Set.of("sad", "down", "unhappy", "low"),
            Emotion.ANGER, Set.of("angry", "mad", "frustrated", "irritated"),
            Emotion.HAPPY, Set.of("happy", "good", "great", "fine"),
            Emotion.TIRED, Set.of("tired", "exhausted", "drained", "fatigued")

    );

    public Emotion detectEmotion(String input) {
    if (input == null || input.isBlank()) return Emotion.UNKNOWN;

    // normalise
    String cleaned = input.toLowerCase().trim();
    cleaned = NON_LETTER.matcher(cleaned).replaceAll(" ");
    cleaned = cleaned.replaceAll("\\s+", " ");

    // tokenise and count matches per emotion
    String[] tokens = cleaned.split(" ");
    Map<Emotion, Integer> counts = new EnumMap<>(Emotion.class);
    for (Emotion e : Emotion.values()) counts.put(e, 0);

    for (String t : tokens) {
    if (t.isBlank()) continue;
    for (var entry : keywords.entrySet()) {
    if (entry.getValue().contains(t)) {
    counts.put(entry.getKey(), counts.get(entry.getKey()) + 1);
    }
    }
    }

    // pick the best match
    Emotion best = Emotion.UNKNOWN;
    int bestScore = 0;

    for (var e : counts.entrySet()) {
    if (e.getKey() == Emotion.UNKNOWN) continue;
    if (e.getValue() > bestScore) {
    bestScore = e.getValue();
    best = e.getKey();
    }
    }

return (bestScore >= 1) ? best : Emotion.UNKNOWN;
}
}

