package com.remidiousE.controller;

import com.remidiousE.dto.request.QuestionWrapper;
import com.remidiousE.dto.response.QuizResponse;
import com.remidiousE.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("quiz")
public class QuizController {

    private final QuizService quizIService;

    @PostMapping("/create")
    public ResponseEntity<String>createQuiz(@Valid @RequestBody String category, @RequestBody String title ){
        return quizIService.createQuiz(category, title) ;
    }

    @GetMapping("get-quiz/{id}")
    public ResponseEntity<List<QuestionWrapper>>getQuizQuestions(@PathVariable Long id) throws Exception {
    return quizIService.getQuizQuestion(id);
    }


    @PostMapping("/submit/{id}")
    public ResponseEntity<Integer>submitQuiz(@PathVariable Long id, @RequestBody List<QuizResponse> responses){
    return quizIService.calculateResult(id, responses);
    }
}