package com.remidiousE.service;

import com.remidiousE.dto.request.QuestionAddingRequest;
import com.remidiousE.model.Question;
import com.remidiousE.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServices implements QuestionService {

    private final QuestionRepository questionRepository;

    private final ModelMapper modelMapper;
    @Override
    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionRepository.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionRepository.findByCategory(category), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> addQuestion(QuestionAddingRequest questionAddingRequest) {
        try {
            Question question = new Question();
            modelMapper.map(questionAddingRequest, question);
            questionRepository.save(question);
            return new ResponseEntity<>( "Question has been added successfully", HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Question has not been added", HttpStatus.BAD_REQUEST);
    }

    @Override
    public String deleteQuestionById(Long id) {
        questionRepository.deleteById(id);
        return "Deleted";
    }


}
