package com.sparta26.baemin.category.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import com.sparta26.baemin.store.entity.Store;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name="p_CATEGORIES")
@Getter
@NoArgsConstructor
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
        this.id = UUID.randomUUID();
        this.name = name;
        this.store = store;
    }

}
