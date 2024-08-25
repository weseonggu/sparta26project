package com.sparta26.baemin.product.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.exception.NotEnoughStockException;
import com.sparta26.baemin.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_PRODUCTS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends AuditEntity {

    @Id
    @GeneratedValue @UuidGenerator
    @Column(name = "product_id")
    private UUID id;

    @Column(nullable = false)
    private String name;
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    private ProductCategory category; // 어떤 종류???

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_available")
    private String isAvailable; // ERD varchar 어떤 상태????

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public Product(String name, String description, Integer price, Integer stockQuantity, ProductCategory category, String imageUrl, String isAvailable, Store store) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable;
        if (store != null) {
            addStore(store);
        }
    }

    //== 생성 메서드 ==//
    public static Product createProduct(String name, String description, Integer price, Integer stockQuantity, ProductCategory category, String imageUrl, String isAvailable, Store store) {
        return new Product(name, description, price, stockQuantity, category, imageUrl, isAvailable, store);
    }


    public void addStore(Store store) {
        this.store = store;
        store.getProducts().add(this);
    }

   // stock 증가 메서드
    public Integer addStock(Integer quantity) {
        this.stockQuantity += quantity;
        return this.stockQuantity;
    }

    // stock 감소 메서드
    public Integer removeStock(Integer stockQuantity) {
        int restStock = this.stockQuantity - stockQuantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
        return restStock;
    }
}
