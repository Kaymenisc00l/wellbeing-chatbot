package com.chatbot.wellbeingchatbot.model;
import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
public class MoodEntry { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String emotion;
    private String category;
    private LocalDate date;

    public MoodEntry(){}

        public MoodEntry(String emotion, String category, LocalDate date){
            this.emotion = emotion;
            this.category = category;
            this.date = date;

        }

        public Long getID(){
            return id;
        }

        public String getEmotion(){
            return emotion;
        }

        public String getCategory(){
            return category;
        }

        public LocalDate getDate(){
            return date;
        }

    

}


