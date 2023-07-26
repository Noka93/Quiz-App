package com.remidiousE.service;

import com.remidiousE.dto.request.QuestionWrapper;
import com.remidiousE.model.Question;
import com.remidiousE.model.Quiz;
import com.remidiousE.dto.response.QuizResponse;
import com.remidiousE.repository.QuestionRepository;
import com.remidiousE.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class QuizServicesTest {

    private QuizServices quizServices;
    private QuizRepository quizRepository;
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        quizRepository = mock(QuizRepository.class);
        questionRepository = mock(QuestionRepository.class);
        quizServices = new QuizServices(quizRepository, questionRepository);
    }

    @Test
    void createQuiz_WithValidData() {
        String category = "Python";
        String title = "What is Python";

        List<Question> questions = new ArrayList<>();
        when(questionRepository.findRandomQuestionsByCategory(category)).thenReturn(questions);

        Quiz quiz = new Quiz();
        when(quizRepository.save(quiz)).thenReturn(quiz);

        ResponseEntity<String> response = quizServices.createQuiz(category, title);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Quiz created successfully", response.getBody());
    }

    @Test
    void getQuizQuestion() throws Exception {
        Long id = 1L;

        Quiz quiz = new Quiz();
        when(quizRepository.findById(id)).thenReturn(Optional.of(quiz));

        List<Question> questionsFromDB = new ArrayList<>();
        quiz.setQuestions(questionsFromDB);

        ResponseEntity<List<QuestionWrapper>> response = quizServices.getQuizQuestion(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getQuizQuestion_WithInvalidId() {
        Long id = 1L;
        when(quizRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> quizServices.getQuizQuestion(id));
    }

    @Test
    void calculateResult_WithValidIdAndResponses() {
        Long id = 1L;

        Quiz quiz = new Quiz();
        when(quizRepository.findById(id)).thenReturn(Optional.of(quiz));

        List<Question> questions = new ArrayList<>();
        quiz.setQuestions(questions);

        List<QuizResponse> responses = new ArrayList<>();

        ResponseEntity<Integer> response = quizServices.calculateResult(id, responses);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
