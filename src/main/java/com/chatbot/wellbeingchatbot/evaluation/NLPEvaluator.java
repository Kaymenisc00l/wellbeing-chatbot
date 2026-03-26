package com.chatbot.wellbeingchatbot.evaluation;

import com.chatbot.wellbeingchatbot.nlp.*;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.List;

@Component
public class NLPEvaluator {

private final BaseLineNLPService baseline;
private final ImprovedNLPService improved;

public NLPEvaluator(BaseLineNLPService baseline,
ImprovedNLPService improved) {
this.baseline = baseline;
this.improved = improved;
}

@PostConstruct
public void evaluate() {

List<NLPTestCase> dataset = List.of(
new NLPTestCase("I am stressed about deadlines", Emotion.STRESS),
new NLPTestCase("I feel anxious and nervous", Emotion.ANXIETY),
new NLPTestCase("I feel very low and down lately", Emotion.SADNESS),
new NLPTestCase("I am not stressed anymore", Emotion.UNKNOWN),
new NLPTestCase("I am burnt out and exhausted", Emotion.TIRED),
new NLPTestCase("I feel happy and excited", Emotion.HAPPY),
new NLPTestCase("I am angry and frustrated", Emotion.ANGER),
new NLPTestCase("I am panicking about tomorrow", Emotion.ANXIETY),
new NLPTestCase("I am burnt out from work", Emotion.TIRED),
new NLPTestCase("I feel on edge all the time", Emotion.ANXIETY),
new NLPTestCase("I'm overwhelmed with everything", Emotion.STRESS),
new NLPTestCase("I feel suicidal and I want to kill myself due to feeling low", Emotion.CRISIS),
new NLPTestCase("I'm so frustrated with people at work", Emotion.ANGER),
new NLPTestCase("I'm hate how tired I am from my relationships", Emotion.TIRED),
new NLPTestCase("I'm so tired from everything I want to kill myself", Emotion.CRISIS)

);

int baselineCorrect = 0;
int improvedCorrect = 0;

for (NLPTestCase test : dataset) {

Emotion baseResult = baseline.detectEmotion(test.getText());
Emotion improvedResult = improved.detectEmotion(test.getText());


if (baseResult == test.getExpected()) baselineCorrect++;
if (improvedResult == test.getExpected()) improvedCorrect++;

System.out.println("-----------------");
System.out.println("Input: " + test.getText());
System.out.println("Expected: " + test.getExpected());
System.out.println("Baseline: " + baseResult);
System.out.println("Improved: " + improvedResult);
System.out.println("-----------------");
}

double baselineAccuracy =
((double) baselineCorrect / dataset.size())*100;

double improvedAccuracy =
((double) improvedCorrect / dataset.size())*100;

System.out.println("Baseline Accuracy: " + baselineAccuracy);
System.out.println("Improved Accuracy: " + improvedAccuracy);
System.out.println("Total Test Cases: " + dataset.size());
System.out.println("-----------------");
}
}