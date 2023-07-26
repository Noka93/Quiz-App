package com.remidiousE.dto.request;

import lombok.Data;

@Data
public class QuestionAddingRequest {
    private String questionTitle;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String correctAnswer;
    private String category;
    private String difficultyLevel;
}
