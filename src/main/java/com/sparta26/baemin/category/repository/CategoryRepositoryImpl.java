package com.sparta26.baemin.category.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta26.baemin.dto.category.QResponseSearchCategoryDto;
import com.sparta26.baemin.dto.category.RequestSearchCategoryDto;
import com.sparta26.baemin.dto.category.ResponseSearchCategoryDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sparta26.baemin.category.entity.QCategory.category;
import static org.springframework.util.StringUtils.hasText;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CategoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseSearchCategoryDto> findAllCategory(Pageable pageable, RequestSearchCategoryDto condition) {

        JPAQuery<ResponseSearchCategoryDto> query = queryFactory
                .select(new QResponseSearchCategoryDto(
                        category.id,
                        category.name,
                        category.store.id,
                        category.createdAt,
                        category.updatedAt
                ))
                .from(category)
                .where(nameContaions(condition.getName()),
                        category.isPublic.eq(true));

        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            sort.forEach(order -> {
                String property = order.getProperty();
                Sort.Direction direction = order.getDirection();

                if ("createdAt".equals(property)) {
                    query.orderBy(order.isAscending() ? category.createdAt.asc() : category.createdAt.desc());
                } else if ("updatedAt".equals(property)) {
                    query.orderBy(order.isAscending() ? category.updatedAt.asc() : category.updatedAt.desc());
                }
            });
        }

        List<ResponseSearchCategoryDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(category.count())
                .from(category)
                .where(nameContaions(condition.getName()),
                        category.isPublic.eq(true));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private BooleanExpression nameContaions(String name) {
        return hasText(name) ? category.name.contains(name) : null;
    }
}
