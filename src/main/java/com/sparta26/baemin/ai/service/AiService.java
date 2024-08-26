package com.sparta26.baemin.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta26.baemin.ai.entity.Ai;
import com.sparta26.baemin.ai.repository.AiRepository;
import com.sparta26.baemin.dto.ai.RequestAiDto;
import com.sparta26.baemin.dto.ai.ResponseAiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class AiService {

    private final AiRepository aiRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public ResponseAiDto questions(RequestAiDto requestAiDto) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://generativelanguage.googleapis.com")
                .path("/v1beta/models/gemini-1.5-flash-latest:generateContent")
                .queryParam("key", requestAiDto.getApi_key())
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // json body 형식 맞추기
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, Object>> contentsList = new ArrayList<>();

        Map<String, Object> partsMap = new HashMap<>();
        partsMap.put("text", requestAiDto.getQuestion() + " 답변은 50자 이내로 해주세요");

        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("parts", Collections.singletonList(partsMap));

        contentsList.add(contentMap);

        requestBody.put("contents", contentsList);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        String answer = jsonNode.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();

        log.info("answer = " + answer);

        Ai ai = new Ai(requestAiDto.getQuestion() + " 답변은 50자 이내로 해주세요",
                answer,
                "유저이름");

        Ai savedAi = aiRepository.save(ai);

        return new ResponseAiDto(savedAi.getAnswer());
    }
}
