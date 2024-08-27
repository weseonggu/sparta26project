package com.sparta26.baemin.dto.ai;

import com.sparta26.baemin.ai.entity.Ai;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAiPageDto {

    private String ai_id;
    private String question;
    private String answer;

    public ResponseAiPageDto(Ai ai) {
        this.ai_id = ai.getId().toString();
        this.question = ai.getQuestion();
        this.answer = ai.getAnswer();
    }
}
