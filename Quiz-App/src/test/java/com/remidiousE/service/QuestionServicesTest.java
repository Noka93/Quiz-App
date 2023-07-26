package com.remidiousE.service;

import com.remidiousE.dto.request.QuestionAddingRequest;
import com.remidiousE.model.Question;
import com.remidiousE.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest

class QuestionServicesTest {

    @Autowired
    private QuestionServices questionServices;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        questionServices = new QuestionServices(questionRepository, modelMapper);
    }

    @Test
    void getAllQuestions_WithValidData_ReturnsListOfQuestions() {
        ResponseEntity<List<Question>> response = questionServices.getAllQuestions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    void getQuestionsByCategory_WithValidCategory_ReturnsListOfQuestionsByCategory() {
        String category = "Java";

        ResponseEntity<List<Question>> response = questionServices.getQuestionsByCategory(category);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    void addQuestion_WithValidQuestionAddingRequest_ReturnsSuccessResponse() {
        QuestionAddingRequest request = new QuestionAddingRequest();
        request.setQuestionTitle("Is Java A Dynamically typed language?");
        request.setOption1("Yes");
        request.setOption2("No");
        request.setOption3("I don't know");
        request.setOption4("Maybe");
        request.setCorrectAnswer("Yes");
        request.setCategory("Java");
        request.setDifficultyLevel("East");

        ResponseEntity<String> response = questionServices.addQuestion(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Question has been added successfully", response.getBody());
    }

    @Test
    void deleteQuestionById_WithValidId_ReturnsDeletedMessage() {
        Long id = 1L;
        String response = questionServices.deleteQuestionById(id);
        assertEquals("Deleted", response);
    }
}
