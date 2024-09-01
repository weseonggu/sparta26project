package com.sparta26.baemin.product.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.dto.product.RequestProductWithoutStockDto;
import com.sparta26.baemin.exception.exceptionsdefined.NotEnoughStockException;
import com.sparta26.baemin.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    private String category; // 상품 상세분류

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_available")
    private boolean isAvailable = true; // 주문가능 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder(access = AccessLevel.PROTECTED)
    public Product(UUID id, String name, String description, Integer price, Integer stockQuantity, String category, String imageUrl, String email, Store store) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.imageUrl = imageUrl;
        super.addCreatedBy(email);
        if (store != null) {
            addStore(store);
        }
    }

    //== 생성 메서드 ==//
    public static Product createProduct(String name, String description, Integer price, Integer stockQuantity, String category, String imageUrl, String email, Store store) {
        return Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .stockQuantity(stockQuantity)
                .category(category)
                .imageUrl(imageUrl)
                .email(email)
                .store(store)
                .build();
    }

    public static Product createProductWithId(UUID id, String name, String description, Integer price, Integer stockQuantity, String category, String imageUrl) {
        return Product.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .stockQuantity(stockQuantity)
                .category(category)
                .imageUrl(imageUrl)
                .build();
    }

    public void addStore(Store store) {
        this.store = store;
        store.getProducts().add(this);
    }


    /**
     * stock 증가 메서드
     * @param quantity
     * @return stock
     */
    public Integer addStock(Integer quantity) {
        if (quantity != null && quantity < 1) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.stockQuantity += quantity;
        return this.stockQuantity;
    }

    /**
     * stock 감소 메서드
     * @param stockQuantity 감소할 수
     * @return 수량
     * @throws NotEnoughStockException 호출한곳에서 예외처리 필요
     */
    public Integer removeStock(Integer stockQuantity) throws NotEnoughStockException {
        int restStock = this.stockQuantity - stockQuantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
        return restStock;
    }

    public void changeAvailable() {
        this.isAvailable = false;
    }

    public Product update(RequestProductWithoutStockDto request) {
        if (request.getName() != null) {
            this.name = request.getName();
        }
        if (request.getDescription() != null) {
            this.description = request.getDescription();
        }
        if (request.getPrice() != null) {
            this.price = request.getPrice();
        }
        if (request.getCategory() != null) {
            this.category = request.getCategory();
        }
        if (request.getImageUrl() != null) {
            this.imageUrl = request.getImageUrl();
        }
        return this;
    }

    public void changeIsAvailable(boolean isAvailable) {
        if (isAvailable) {
            this.isAvailable = true;
        } else {
            this.isAvailable = false;
        }
    }
}
