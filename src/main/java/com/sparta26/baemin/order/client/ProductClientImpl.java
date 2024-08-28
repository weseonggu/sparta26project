package com.sparta26.baemin.order.client;

import com.sparta26.baemin.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient{

    Product product;

    //    private final ProductService productService;
    @Override
    public Product getProductById(String productId) {
        //TODO ProductService 에서 productId 로 상품정보를 가져오는 메서드
        return product;
    }
}
