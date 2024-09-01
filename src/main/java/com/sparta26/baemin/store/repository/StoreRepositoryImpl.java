package com.sparta26.baemin.store.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta26.baemin.dto.category.ResponseCategoryWithOutStoreIdDto;
import com.sparta26.baemin.dto.deliveryzone.ResponseDeliveryWithOutStoreIdDto;
import com.sparta26.baemin.dto.operatinghours.ResponseOperatingDto;
import com.sparta26.baemin.dto.product.QResponseProductWithOutStoreIdDto;
import com.sparta26.baemin.dto.product.ResponseProductWithOutStoreIdDto;
import com.sparta26.baemin.dto.store.RequestSearchStoreDto;
import com.sparta26.baemin.dto.store.ResponseSearchStoreDto;
import com.sparta26.baemin.store.entity.Store;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.UUID;

import static com.sparta26.baemin.category.entity.QCategory.category;
import static com.sparta26.baemin.deliveryzone.entity.QDeliveryZone.deliveryZone;
import static com.sparta26.baemin.operatinghours.entity.QOperatingHours.operatingHours;
import static com.sparta26.baemin.product.entity.QProduct.product;
import static com.sparta26.baemin.store.entity.QStore.store;
import static org.springframework.util.StringUtils.hasText;

public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public StoreRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public ResponseSearchStoreDto findOneStore(UUID storeId) {

        // 메인 스토어 정보 가져오기
        Store storeEntity = queryFactory
                .selectFrom(store)
                .where(store.id.eq(storeId),
                        store.isPublic.eq(true))
                .fetchOne();

        if (storeEntity == null) {
            return null;
        }

        // 제품 리스트 가져오기
        List<ResponseProductWithOutStoreIdDto> products = queryFactory
                .select(
                        new QResponseProductWithOutStoreIdDto(
                                product.id,
                                product.name,
                                product.description,
                                product.price,
                                product.stockQuantity,
                                product.category,
                                product.imageUrl,
                                product.isAvailable
                        )
                )
                .from(product)
                .where(product.store.id.eq(storeId),
                        product.isPublic.eq(true))
                .fetch();

        // 운영 시간 리스트 가져오기
        List<ResponseOperatingDto> findOperating = queryFactory
                .select(
                        Projections.constructor(
                                ResponseOperatingDto.class,
                                operatingHours.id,
                                operatingHours.openingTime,
                                operatingHours.closingTime,
                                operatingHours.openDays,
                                operatingHours.lastOrder
                        )
                )
                .from(operatingHours)
                .where(operatingHours.store.id.eq(storeId),
                        operatingHours.isPublic.eq(true))
                .fetch();

        // 배달 지역 리스트 가져오기
        List<ResponseDeliveryWithOutStoreIdDto> findDelivery = queryFactory
                .select(
                        Projections.constructor(
                                ResponseDeliveryWithOutStoreIdDto.class,
                                deliveryZone.id,
                                deliveryZone.name,
                                deliveryZone.isPossible
                        )
                )
                .from(deliveryZone)
                .where(deliveryZone.store.id.eq(storeId),
                        deliveryZone.isPublic.eq(true))
                .fetch();

        // 카테고리 리스트 가져오기
        List<ResponseCategoryWithOutStoreIdDto> findCategory = queryFactory
                .select(
                        Projections.constructor(
                                ResponseCategoryWithOutStoreIdDto.class,
                                category.id,
                                category.name
                        )
                )
                .from(category)
                .where(category.store.id.eq(storeId),
                        category.isPublic.eq(true))
                .fetch();

        return new ResponseSearchStoreDto(
                storeEntity.getId(),
                storeEntity.getName(),
                storeEntity.getDescription(),
                storeEntity.getAddress(),
                storeEntity.getPhoneNumber(),
                storeEntity.isActive(),
                storeEntity.getMember().getId(),
                storeEntity.getCreatedAt(),
                storeEntity.getUpdatedAt(),
                products,
                findOperating,
                findDelivery,
                findCategory
        );

    }


    @Override
    public Page<ResponseSearchStoreDto> findPagingAll(Pageable pageable, RequestSearchStoreDto condition) {

        JPAQuery<Store> query = queryFactory
                .select(store)
                .from(store)
                .where(nameContains(condition.getName()),
                        store.isPublic.eq(true));


        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            sort.forEach(order -> {
                String property = order.getProperty();
                Sort.Direction direction = order.getDirection();

                if ("createdAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? store.createdAt.asc() : store.createdAt.desc());
                } else if ("updatedAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? store.updatedAt.asc() : store.updatedAt.desc());
                }
            });
        }

        List<Store> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(store.count())
                .from(store)
                .where(nameContains(condition.getName()),
                        store.isPublic.eq(true));

        List<ResponseSearchStoreDto> result = ResponseSearchStoreDto.toDtoList(content);
        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchCount);
    }

    private BooleanExpression nameContains(String name) {
        return hasText(name) ? store.name.contains(name) : null;
    }

}
