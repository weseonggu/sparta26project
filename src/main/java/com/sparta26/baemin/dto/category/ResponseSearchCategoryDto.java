package com.sparta26.baemin.dto.category;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
public class ResponseSearchCategoryDto {

    private UUID category_id;
    private String name;
    private UUID store_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @QueryProjection
    public ResponseSearchCategoryDto(UUID category_id, String name, UUID store_id, LocalDateTime created_at, LocalDateTime updated_at) {
        this.category_id = category_id;
        this.name = name;
        this.store_id = store_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
