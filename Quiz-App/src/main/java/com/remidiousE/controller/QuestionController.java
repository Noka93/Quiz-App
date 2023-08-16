package com.remidiousE.controller;

import com.remidiousE.dto.request.QuestionAddingRequest;
import com.remidiousE.model.Question;
import com.remidiousE.service.QuestionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class  QuestionController {

    private final QuestionService questionService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

@PostMapping("add")
public ResponseEntity<String> addQuestions(@Valid @RequestBody QuestionAddingRequest question){
    return questionService.addQuestion(question);
}

@GetMapping("getQuestions")
public ResponseEntity<List<Question>> getAllQuestions(){
    return questionService.getAllQuestions();
}

@GetMapping("category/{category}")
public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category){
    return questionService.getQuestionsByCategory(category);
}

@DeleteMapping("deleteQuestion/{id}")
public String deleteQuestionById(@PathVariable Long id){
    return questionService.deleteQuestionById(id);
}
}
