package com.sparta26.baemin.product.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta26.baemin.dto.product.QResponseProductDto;
import com.sparta26.baemin.dto.product.RequestSearchProductDto;
import com.sparta26.baemin.dto.product.ResponseProductDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sparta26.baemin.product.entity.QProduct.product;
import static org.springframework.util.StringUtils.hasText;

public class ProductRepositoryImpl implements ProductRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseProductDto> findAllProduct(Pageable pageable, RequestSearchProductDto condition) {

        JPAQuery<ResponseProductDto> query = queryFactory
                .select(new QResponseProductDto(
                        product.id,
                        product.name,
                        product.description,
                        product.price,
                        product.stockQuantity,
                        product.category,
                        product.imageUrl,
                        product.isAvailable,
                        product.createdAt,
                        product.updatedAt,
                        product.store.id
                ))
                .from(product)
                .where(nameContains(condition.getName()),
                        product.isPublic.eq(true));

        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            sort.forEach(order -> {
                String property = order.getProperty();
                Sort.Direction direction = order.getDirection();

                if ("createdAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? product.createdAt.asc() : product.createdAt.desc());
                } else if ("updatedAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? product.updatedAt.asc() : product.updatedAt.desc());
                }
            });
        }

        List<ResponseProductDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(product.count())
                .from(product)
                .where(nameContains(condition.getName()),
                        product.isPublic.eq(true));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private BooleanExpression nameContains(String name) {
        return hasText(name) ? product.name.contains(name) : null;
    }
}
