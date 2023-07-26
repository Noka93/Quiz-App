package com.remidiousE.service;

import com.remidiousE.dto.request.QuestionAddingRequest;
import com.remidiousE.model.Question;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuestionService {
    ResponseEntity<List<Question>> getAllQuestions();

    ResponseEntity<String> addQuestion(QuestionAddingRequest questionAddingRequest);

    String deleteQuestionById(Long id);

    ResponseEntity<List<Question>> getQuestionsByCategory(String category);
}
