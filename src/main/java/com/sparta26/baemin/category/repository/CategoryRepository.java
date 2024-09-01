package com.sparta26.baemin.category.repository;

import com.sparta26.baemin.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID>, CategoryRepositoryCustom {

    @Query("select c from Category c where c.id = :categoryId and c.createdBy = :email and c.isPublic = true")
    Optional<Category> findByIdAndCreatedBy(@Param("categoryId") UUID categoryId,@Param("email") String email);

    @Query("select c from Category c where c.store.id = :storeId and c.name = :name and c.isPublic = true")
    Optional<Category> findByStoreIdAndName(@Param("storeId") UUID storeId, @Param("name") String name);

    @Query("select c from Category c where c.store.id = :storeId and c.name <> :name and c.isPublic = true")
    List<Category> findDuplicatedByStoreIdAndName(@Param("storeId") UUID id,@Param("name") String name);
}
