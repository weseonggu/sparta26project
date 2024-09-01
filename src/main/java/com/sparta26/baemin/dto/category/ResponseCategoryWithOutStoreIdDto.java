package com.sparta26.baemin.dto.category;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta26.baemin.category.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
public class ResponseCategoryWithOutStoreIdDto {

    private UUID category_id;
    private String name;

    @QueryProjection
    public ResponseCategoryWithOutStoreIdDto(UUID category_id, String name) {
        this.category_id = category_id;
        this.name = name;
    }

    public static List<ResponseCategoryWithOutStoreIdDto> toDtoList(List<Category> categories) {
        List<ResponseCategoryWithOutStoreIdDto> dtos = new ArrayList<ResponseCategoryWithOutStoreIdDto>();
        for (Category category : categories) {
            if (category.isPublic()) {
                dtos.add(new ResponseCategoryWithOutStoreIdDto(category.getId(), category.getName()));
            }
        }
        return dtos;
    }
}
