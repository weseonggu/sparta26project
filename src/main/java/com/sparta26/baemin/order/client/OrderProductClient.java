package com.sparta26.baemin.order.client;

import com.sparta26.baemin.product.entity.Product;

public interface OrderProductClient {
    Product getProductById(String productId);
}
