package com.chatbot.wellbeingchatbot.nlp;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class ImprovedNLPService {

private static final Pattern NON_LETTER = Pattern.compile("[^a-z\\s]");
private static final Set<String> NEGATIONS = Set.of("not", "no", "never", "dont", "don't", "cannot", "can't");

// crisis keywords 
private static final Set<String> CRISIS = Set.of(
"suicide", "kill myself", "end my life", "self harm", "self-harm", "cut myself", "overdose");

private final BaseLineNLPService baseline;

// Weightings 
private final Map<Emotion, Map<String, Integer>> extraTokenWeights = Map.of(
Emotion.ANXIETY, Map.of("panicking", 3, "uneasy", 2, "anxious", 3, "nervous", 2, "worried", 2, "edge", 3),
Emotion.SADNESS, Map.of("empty", 2, "lonely", 3, "low", 2, "down", 2, "sad", 3),
Emotion.TIRED, Map.of("sleepy", 2, "insomnia", 3, "exhausted", 3, "burnt", 2, "out", 1),
Emotion.HAPPY, Map.of("relieved", 2, "proud", 2, "happy", 3, "excited", 2),
Emotion.ANGER, Map.of("rage", 3, "angry", 3, "frustrated", 3)
);

private final Set<String> stopwords = Set.of(
    "i", "im", "i'm", "am", "the", "a", "an", "and", "or", "but", "to", "of", "in", "on", "at", "it", "this", "that", "is", "are", "feeling", "feel", "really", "very", "so"
);

public ImprovedNLPService(BaseLineNLPService baseline) {
this.baseline = baseline;
}

/**
* Improved emotion detection.
* - crisis override: return UNKNOWN (so controller can show safety message)
* - negation handling: reduce score when emotion words are negated
* - combine baseline result with extra scoring
*/
public Emotion detectEmotion(String input) {
if (input == null || input.isBlank()) return Emotion.UNKNOWN;

String text = normalise(input);

// 1) crisis override
if (containsCrisis(text)) {
    System.out.println ("CRISIS DETECTED");
return Emotion.CRISIS;

}

// 2) start with baseline emotion (your KeywordNLPService)
Emotion base = baseline.detectEmotion(input);

// 3) extra scoring layer + negation
Map<Emotion, Integer> scores = new EnumMap<>(Emotion.class);
for (Emotion e : Emotion.values()) scores.put(e, 0);

// small boost for baseline prediction so it still matters
if (base != Emotion.UNKNOWN) {
scores.put(base, scores.get(base) + 2);
}



List<String> tokens = tokenise(text);

for (var entry : extraTokenWeights.entrySet()) {
Emotion emotion = entry.getKey();
Map<String, Integer> weights = entry.getValue();

for (int i = 0; i < tokens.size(); i++) {
String t = tokens.get(i);
Integer w = weights.get(t);
if (w == null) continue;

// negation check in a window 
boolean negated = isNegated(tokens, i);

if (negated) {
// reduce/penalise rather than add
scores.put(emotion, scores.get(emotion) - w);
} else {
scores.put(emotion, scores.get(emotion) + w);
}
}
}

// 4) choose best emotion
Emotion best = Emotion.UNKNOWN;
int bestScore = Integer.MIN_VALUE;

for (var s : scores.entrySet()) {
if (s.getKey() == Emotion.UNKNOWN) continue;
if (s.getValue() > bestScore) {
bestScore = s.getValue();
best = s.getKey();
}
}

// threshold: if weak, fall back to baseline
if (bestScore < 1) return base;
return best;
}

private boolean containsCrisis(String text) {
for (String c : CRISIS) {
if (text.contains(c)) return true;
}
return false;
}

private boolean isNegated(List<String> tokens, int index) {
int start = Math.max(0, index - 3);
for (int i = start; i < index; i++) {
if (NEGATIONS.contains(tokens.get(i))) return true;
}
return false;
}

private String normalise(String input) {
String t = input.toLowerCase().trim();
t = NON_LETTER.matcher(t).replaceAll(" ");
t = t.replaceAll("\\s+", " ");
return t;
}

private List<String> tokenise(String text) {
String[] raw = text.split("\\s+");
List<String> tokens = new ArrayList<>(raw.length);
for (String r : raw){
    if(r.isBlank()) continue;
    if (stopwords.contains(r)) continue;
    tokens.add(r);
}
return tokens;
}
}