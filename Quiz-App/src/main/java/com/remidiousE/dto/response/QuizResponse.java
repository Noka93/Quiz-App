package com.remidiousE.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class QuizResponse {
    private Long id;
    private String answer;
}
