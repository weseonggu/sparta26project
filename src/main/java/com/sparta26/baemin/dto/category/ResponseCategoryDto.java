package com.sparta26.baemin.dto.category;

import com.sparta26.baemin.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCategoryDto {

    private UUID category_id;
    private String name;
    private UUID store_id;

    public static ResponseCategoryDto toDto(Category savedCategory) {
        return new ResponseCategoryDto(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getStore().getId()
        );
    }
}
