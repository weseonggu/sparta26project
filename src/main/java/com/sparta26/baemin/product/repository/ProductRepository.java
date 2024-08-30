package com.sparta26.baemin.product.repository;

import com.sparta26.baemin.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {

    @Override
    @Query("select p from Product p where p.id = :id and p.isPublic = true")
    Optional<Product> findById(@Param("id") UUID uuid);

    @Query("select p from Product p where p.id = :id and p.store.createdBy = :createdBy and p.isPublic = true")
    Optional<Product> findByIdAndCreatedBy(@Param("id") UUID uuid,@Param("createdBy") String email);
}
