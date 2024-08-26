package com.sparta26.baemin.store.controller;

import com.sparta26.baemin.dto.store.RequestStoreDto;
import com.sparta26.baemin.dto.store.ResponseStoreDto;
import com.sparta26.baemin.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class StoreController {

    private final StoreService storeService;

    // 가게 생성
    @PostMapping("/store/{memberId}")
    public ResponseEntity<?> createStore(@Valid @RequestBody RequestStoreDto requestStoreDto,
                                         @PathVariable Long memberId) {

        ResponseStoreDto responseStoreDto = storeService.createStore(requestStoreDto, memberId);
        return ResponseEntity.ok(responseStoreDto);
    }

    // 가게 단건 조회
    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> findOneStore(@PathVariable("storeId") String storeId) {

        ResponseStoreDto responseStoreDto = storeService.findOneStore(storeId);
        return ResponseEntity.ok(responseStoreDto);
    }

    // 가게 전체 조회
    


}
