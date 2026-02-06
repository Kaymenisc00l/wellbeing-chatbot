package com.chatbot.repository;

import com.chatbot.wellbeingchatbot.model.MoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoodRepository extends JpaRepository<MoodEntry, Long> {
    
    
}
