package com.sparta26.baemin.deliveryzone.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta26.baemin.dto.deliveryzone.QResponseSearchDeliveryDto;
import com.sparta26.baemin.dto.deliveryzone.RequestSearchDeliveryDto;
import com.sparta26.baemin.dto.deliveryzone.ResponseSearchDeliveryDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sparta26.baemin.deliveryzone.entity.QDeliveryZone.deliveryZone;
import static org.springframework.util.StringUtils.hasText;

public class DeliveryZoneRepositoryImpl implements DeliveryZoneRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public DeliveryZoneRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseSearchDeliveryDto> findAllDelivery(RequestSearchDeliveryDto condition, Pageable pageable) {

        JPAQuery<ResponseSearchDeliveryDto> query = queryFactory
                .select(new QResponseSearchDeliveryDto(
                        deliveryZone.id,
                        deliveryZone.name,
                        deliveryZone.isPossible,
                        deliveryZone.store.id,
                        deliveryZone.createdAt,
                        deliveryZone.updatedAt
                ))
                .from(deliveryZone)
                .where(nameContains(condition.getName()),
                        deliveryZone.isPublic.eq(true));

        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            sort.forEach(order -> {
                String property = order.getProperty();
                Sort.Direction direction = order.getDirection();

                if ("createdAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? deliveryZone.createdAt.asc() : deliveryZone.createdAt.desc());
                }else if ("updatedAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? deliveryZone.createdAt.asc() : deliveryZone.createdAt.desc());
                }
            });
        }

        List<ResponseSearchDeliveryDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(deliveryZone.count())
                .from(deliveryZone)
                .where(nameContains(condition.getName()),
                        deliveryZone.isPublic.eq(true));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private BooleanExpression nameContains(String name) {
        return hasText(name) ? deliveryZone.name.contains(name) : null;
    }
}
