package com.remidiousE.service;

import com.remidiousE.dto.request.QuestionWrapper;
import com.remidiousE.dto.request.QuizRequest;
import com.remidiousE.dto.response.QuizResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuizService {

    ResponseEntity<String> createQuiz(QuizRequest request);

    ResponseEntity<List<QuestionWrapper>> getQuizQuestion(Long id) throws Exception;

    ResponseEntity<Integer> calculateResult(Long id, List<QuizResponse> responses);
}
