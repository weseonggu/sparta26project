package com.sparta26.baemin.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta26.baemin.product.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ResponseProductDto {

    private UUID product_id;
    private String name;
    private String description;

    private Integer price;
    private Integer stock_quantity;

    private String category;
    private String imageUrl;
    private boolean is_available;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private UUID store_id;

    @QueryProjection
    public ResponseProductDto(UUID product_id, String name, String description, Integer price, Integer stock_quantity, String category, String imageUrl, boolean is_available, LocalDateTime created_at, LocalDateTime updated_at, UUID store_id) {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock_quantity = stock_quantity;
        this.category = category;
        this.imageUrl = imageUrl;
        this.is_available = is_available;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.store_id = store_id;
    }

    public static ResponseProductDto toDto(Product savedProduct) {
        return new ResponseProductDto(savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getPrice(),
                savedProduct.getStockQuantity(),
                savedProduct.getCategory(),
                savedProduct.getImageUrl(),
                savedProduct.isAvailable(),
                savedProduct.getCreatedAt(),
                savedProduct.getUpdatedAt(),
                savedProduct.getStore().getId());
    }
}
