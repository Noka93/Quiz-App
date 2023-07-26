package com.remidiousE.service;

import com.remidiousE.dto.request.QuestionWrapper;
import com.remidiousE.model.Question;
import com.remidiousE.model.Quiz;
import com.remidiousE.dto.response.QuizResponse;
import com.remidiousE.repository.QuestionRepository;
import com.remidiousE.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServices implements QuizService {

    private final QuizRepository quizRepository;

    private final QuestionRepository questionRepository;
    @Override
    public ResponseEntity<String> createQuiz(String category, String title) {
        List<Question> questions = questionRepository.findRandomQuestionsByCategory(category);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizRepository.save(quiz);
        return new ResponseEntity<>("Quiz created successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestion(Long id) throws Exception {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) throw new Exception("Quiz not found with id: " + id);
        List<Question> questionFomDB = quiz.get().getQuestions();
        List<QuestionWrapper>questionsForUsers = new ArrayList<>();
        for (Question question:questionFomDB){
            QuestionWrapper questionWrapper = new QuestionWrapper(question.getQuestionId(), question.getQuestionTitle(), question.getOption1(), question.getOption2(), question.getOption3(), question.getOption4());
            questionsForUsers.add(questionWrapper);
        }
        return new ResponseEntity<>(questionsForUsers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> calculateResult(Long id, List<QuizResponse> responses) {
       Optional<Quiz> quiz = quizRepository.findById(id);
        List<Question> questions = quiz.get().getQuestions();

        int rightAnswer = 0;
        int i = 0;
        for (QuizResponse response : responses) {
            if (response.getResponse() != null && response.getResponse().equals(questions.get(i).getCorrectAnswer()))
                rightAnswer++;

            i++;
        }
        return new ResponseEntity<>(rightAnswer, HttpStatus.OK);
    }

}
