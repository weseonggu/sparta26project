package com.sparta26.baemin.order.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta26.baemin.member.entity.UserRole;
import com.sparta26.baemin.order.entity.Order;
import com.sparta26.baemin.order.entity.OrderStatus;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;
import static com.sparta26.baemin.order.entity.QOrder.order;
import static com.sparta26.baemin.orderproduct.entity.QOrderProduct.orderProduct;
import static com.sparta26.baemin.product.entity.QProduct.product;
import static com.sparta26.baemin.store.entity.QStore.store;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    public OrderRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public Page<Order> getOrdersByRole(
            String status, String search, String role, String email, Pageable pageable
    ) {

        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

        JPAQuery<Order> query = queryFactory
                .selectFrom(order)
                .join(order.store, store)
                .leftJoin(order.orderProducts, orderProduct)
                .leftJoin(orderProduct.product, product);

        // 조건부 필터
        if (StringUtils.hasText(search) && !search.trim().isEmpty()) {
            query
                    .where(
                            store.name.containsIgnoreCase(search)
                                    .or(product.name.containsIgnoreCase(search))
                    );
        }

        query.where(
                        userCheck(role, email),
                        statusEq(status),
                        order.isPublic.eq(true)
                )
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Order> orderList = query.fetch();

        return new PageImpl<>(orderList, pageable, orderList.size());
    }

    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();
        orders.add(new OrderSpecifier<>(DESC, order.createdAt));
        orders.add(new OrderSpecifier<>(DESC, order.updatedAt));

        for (Sort.Order sortOrder : pageable.getSort()) {
            com.querydsl.core.types.Order direction =
                    sortOrder.isAscending() ? ASC : DESC;
            switch (sortOrder.getProperty()) {
                case "createdAt" ->
                        orders.set(0, new OrderSpecifier<>(direction, order.createdAt));
                case "updatedAt" ->
                        orders.set(1, new OrderSpecifier<>(direction, order.updatedAt));
                default ->
                        orders.add(new OrderSpecifier<>(
                            direction,
                            Expressions.stringPath(sortOrder.getProperty())
                        ));
            }
        }

        return orders;
    }

    private BooleanExpression userCheck(String role, String email) {

        // 권한에 따라 조건 설정
        return switch (UserRole.fromString(role)) {
            case ROLE_CUSTOMER -> order.createdBy.eq(email);
            case ROLE_OWNER -> order.store.createdBy.eq(email);
            case ROLE_MANAGER, ROLE_MASTER -> null;
        };
    }

    private BooleanExpression statusEq(String status) {
        return status != null ? order.status.eq(OrderStatus.fromString(status)) : null;
    }
}
