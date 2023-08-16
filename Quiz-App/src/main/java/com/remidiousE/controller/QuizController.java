package com.remidiousE.controller;

import com.remidiousE.dto.request.QuestionWrapper;
import com.remidiousE.dto.request.QuizRequest;
import com.remidiousE.dto.response.QuizResponse;
import com.remidiousE.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
//@CrossOrigin
public class QuizController {

    private final QuizService quizIService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/create")
    public ResponseEntity<String>createQuiz(@Valid @RequestBody QuizRequest request){
        return quizIService.createQuiz(request) ;
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
