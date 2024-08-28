package com.sparta26.baemin.order.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta26.baemin.exception.exceptionsdefined.UnauthorizedException;
import com.sparta26.baemin.member.entity.UserRole;
import com.sparta26.baemin.order.entity.Order;
import com.sparta26.baemin.order.entity.OrderStatus;
import com.sparta26.baemin.order.entity.QOrder;
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
                .join(order.store, store);

        // 조건부 leftJoin
        if (StringUtils.hasText(search) && !search.trim().isEmpty()) {
            query.leftJoin(store.products, product)
                    .where(containsStoreNameOrProductName(search));
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

        if (pageable.getSort() != null) {
            for (Sort.Order sortOrder : pageable.getSort()) {
                com.querydsl.core.types.Order direction =
                        sortOrder.isAscending() ? ASC : DESC;
                switch (sortOrder.getProperty()) {
                    case "createdAt" ->
                            orders.add(new OrderSpecifier<>(direction, order.createdAt));
                    case "updatedAt" ->
                            orders.add(new OrderSpecifier<>(direction, order.updatedAt));
                    default -> {
                    }
                }
            }
        }

        return orders;
    }

    private BooleanExpression userCheck(String role, String email) {
        QOrder order = QOrder.order;

        // 권한에 따라 조건 설정
        if (UserRole.ROLE_CUSTOMER.name().equals(role)) {
            return order.createdBy.eq(email);
        } else if (UserRole.ROLE_OWNER.name().equals(role)) {
            return order.store.createdBy.eq(email);
        } else if (UserRole.ROLE_MANAGER.name().equals(role) ||
                UserRole.ROLE_MASTER.name().equals(role)) {
            return null;
        } else {
            throw new UnauthorizedException("Required permissions to access this resource");
        }
    }

    private BooleanExpression statusEq(String status) {
        return status != null ? order.status.eq(OrderStatus.valueOf(status)) : null;
    }

    private BooleanExpression containsStoreNameOrProductName(String search) {
        return store.name.containsIgnoreCase(search)
                .or(product.name.containsIgnoreCase(search));
    }
}
