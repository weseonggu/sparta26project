package com.sparta26.baemin.order.client;

import com.sparta26.baemin.dto.product.ResponseProductDto;
import com.sparta26.baemin.product.entity.Product;
import com.sparta26.baemin.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceClientImpl implements ProductServiceClient {

    private final ProductService productService;
    @Override
    public Product getProductById(String productId) {

        ResponseProductDto responseProductDto =
                productService.findOneProduct(UUID.fromString(productId));

        return convertToProduct(responseProductDto);
    }

    private Product convertToProduct(ResponseProductDto response) {
        return Product.createProductWithId(
                response.getProduct_id(),
                response.getName(),
                response.getDescription(),
                response.getPrice(),
                response.getStock_quantity(),
                response.getCategory(),
                response.getImageUrl()
        );
    }
}
