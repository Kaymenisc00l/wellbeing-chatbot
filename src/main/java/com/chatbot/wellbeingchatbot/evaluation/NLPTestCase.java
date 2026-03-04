package com.chatbot.wellbeingchatbot.evaluation;

import com.chatbot.wellbeingchatbot.nlp.Emotion;

public class NLPTestCase {

private final String text;
private final Emotion expected;

public NLPTestCase(String text, Emotion expected) {
this.text = text;
this.expected = expected;
}

public String getText() { return text; }
public Emotion getExpected() { return expected; }
}