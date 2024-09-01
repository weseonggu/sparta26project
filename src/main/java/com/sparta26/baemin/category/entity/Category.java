package com.sparta26.baemin.category.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.dto.category.RequestCategoryDto;
import com.sparta26.baemin.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name="p_CATEGORIES")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends AuditEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "category_id")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public Category(String name, Store store) {
        this.name = name;
        if (store != null) {
            this.store = store;
        }
    }

    public static Category createCategory(String name, Store store) {
        return new Category(name, store);
    }

    public void addStore(Store store) {
        this.store = store;
        store.getCategories().add(this);
    }

    public Category update(RequestCategoryDto request) {
        if (request.getName() != null) {
            this.name = request.getName();
        }
        return this;
    }
}
