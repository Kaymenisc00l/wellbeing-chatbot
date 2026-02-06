package com.chatbot.wellbeingchatbot.api;

import com.chatbot.wellbeingchatbot.model.MoodEntry;
import com.chatbot.wellbeingchatbot.repository.MoodRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/mood")
public class MoodController {

    private final MoodRepository moodRepository;

    public MoodController(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }

    // Save mood entry
    @PostMapping("/save")
    public MoodEntry saveMood(@RequestBody MoodEntry mood) {

        MoodEntry newMood = new MoodEntry(
                mood.getEmotion(),
                mood.getCategory(),
                LocalDate.now()
        );

        return moodRepository.save(newMood);
    }

    // Get all stored moods
    @GetMapping("/all")
    public List<MoodEntry> getAllMoods() {
        return moodRepository.findAll();
    }
}

