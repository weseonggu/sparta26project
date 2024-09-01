package com.sparta26.baemin.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta26.baemin.product.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ResponseProductWithOutStoreIdDto {

    private UUID product_id;
    private String name;
    private String description;

    private Integer price;
    private Integer stock_quantity;

    private String category;
    private String imageUrl;
    private boolean is_available;

    @QueryProjection
    public ResponseProductWithOutStoreIdDto(UUID product_id, String name, String description, Integer price, Integer stock_quantity, String category, String imageUrl, boolean is_available) {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock_quantity = stock_quantity;
        this.category = category;
        this.imageUrl = imageUrl;
        this.is_available = is_available;
    }

    public static List<ResponseProductWithOutStoreIdDto> toDtoList(List<Product> products) {

        List<ResponseProductWithOutStoreIdDto> dtos = new ArrayList<>();
        for (Product product : products) {
            if (product.isPublic()) {
                ResponseProductWithOutStoreIdDto dto = new ResponseProductWithOutStoreIdDto();
                dto.product_id = product.getId();
                dto.name = product.getName();
                dto.description = product.getDescription();
                dto.price = product.getPrice();
                dto.stock_quantity = product.getStockQuantity();
                dto.category = product.getCategory();
                dto.imageUrl = product.getImageUrl();
                dto.is_available = product.isAvailable();
                dtos.add(dto);
            }
        }
        return dtos;
    }
}
