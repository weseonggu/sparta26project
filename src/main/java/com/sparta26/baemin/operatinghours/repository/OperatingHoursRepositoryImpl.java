package com.sparta26.baemin.operatinghours.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta26.baemin.dto.operatinghours.QResponseSearchOperatingDto;
import com.sparta26.baemin.dto.operatinghours.RequestSearchOperatingDto;
import com.sparta26.baemin.dto.operatinghours.ResponseSearchOperatingDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sparta26.baemin.operatinghours.entity.QOperatingHours.operatingHours;
import static com.sparta26.baemin.product.entity.QProduct.product;
import static org.springframework.util.StringUtils.hasText;

public class OperatingHoursRepositoryImpl implements OperatingHoursRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public OperatingHoursRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseSearchOperatingDto> findAllOperating(Pageable pageable, RequestSearchOperatingDto condition) {

        JPAQuery<ResponseSearchOperatingDto> query = queryFactory
                .select(new QResponseSearchOperatingDto(
                        operatingHours.id,
                        operatingHours.openingTime,
                        operatingHours.closingTime,
                        operatingHours.openDays,
                        operatingHours.lastOrder,
                        operatingHours.store.id,
                        operatingHours.createdAt,
                        operatingHours.updatedAt
                ))
                .from(operatingHours)
                .where(openTimeEq(condition.getOpening_time()),
                        closeTimeEq(condition.getClosing_time()),
                        openDaysEq(condition.getOpen_days()),
                        lastOrderEq(condition.getLast_order()),
                        operatingHours.isPublic.eq(true));

        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            sort.forEach(order -> {
                String property = order.getProperty();
                Sort.Direction direction = order.getDirection();

                if ("createdAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? operatingHours.createdAt.asc() : product.createdAt.desc());
                } else if ("updatedAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? operatingHours.updatedAt.asc() : product.updatedAt.desc());
                }
            });
        }

        List<ResponseSearchOperatingDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(operatingHours.count())
                .from(operatingHours)
                .where(openTimeEq(condition.getOpening_time()),
                        closeTimeEq(condition.getClosing_time()),
                        openDaysEq(condition.getOpen_days()),
                        lastOrderEq(condition.getLast_order()),
                        operatingHours.isPublic.eq(true));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private BooleanExpression lastOrderEq(String lastOrder) {
        return hasText(lastOrder) ? operatingHours.lastOrder.eq(lastOrder) : null;
    }

    private BooleanExpression openDaysEq(String openDays) {
        return hasText(openDays) ? operatingHours.openDays.eq(openDays) : null;
    }

    private BooleanExpression closeTimeEq(String closingTime) {
        return hasText(closingTime) ? operatingHours.closingTime.eq(closingTime) : null;
    }

    private BooleanExpression openTimeEq(String openingTime) {
        return hasText(openingTime) ? operatingHours.openingTime.eq(openingTime) : null;
    }
}
