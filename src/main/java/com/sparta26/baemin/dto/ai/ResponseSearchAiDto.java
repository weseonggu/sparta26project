package com.sparta26.baemin.dto.ai;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ResponseSearchAiDto {

    private UUID ai_id;
    private String question;
    private String answer;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @QueryProjection
    public ResponseSearchAiDto(UUID ai_id, String question, String answer, LocalDateTime created_at, LocalDateTime updated_at) {
        this.ai_id = ai_id;
        this.question = question;
        this.answer = answer;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
