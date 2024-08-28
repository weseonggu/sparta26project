package com.sparta26.baemin.dto.product;

import com.sparta26.baemin.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProductDto {

    private String product_id;
    private String name;
    private String description;

    private Integer price;
    private Integer stock_quantity;

    private String category;
    private String imageUrl;
    private boolean is_available;

    public static ResponseProductDto toDto(Product savedProduct) {
        return new ResponseProductDto(savedProduct.getId().toString(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getPrice(),
                savedProduct.getStockQuantity(),
                savedProduct.getCategory(),
                savedProduct.getImageUrl(),
                savedProduct.isAvailable());
    }
}
