package com.sparta26.baemin.dto.ai;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestAiDto {

    @NotBlank(message = "질문 사항은 반드시 입력하셔야 합니다.")
    private String question;
    @NotBlank(message = "api_key 반드시 입력하셔야 합니다.")
    private String api_key;
}
