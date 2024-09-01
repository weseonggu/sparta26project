package com.sparta26.baemin.payment.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta26.baemin.member.entity.UserRole;
import com.sparta26.baemin.payment.entity.Payment;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;
import static com.sparta26.baemin.payment.entity.QPayment.payment;

@Repository
public class PaymentRepositoryCustomImpl implements  PaymentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PaymentRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public Page<Payment> getPaymentsByRole(
            LocalDate startDate, LocalDate endDate, Pageable pageable,
            String email, String role
    ) {

        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

        JPAQuery<Payment> query = queryFactory
                .selectFrom(payment)
                .where(
                        userCheck(role, email),
                        dateBetween(startDate, endDate),
                        payment.isPublic.eq(true)
                )
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Payment> paymentList = query.fetch();

        return new PageImpl<>(paymentList, pageable, paymentList.size());
    }

    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();
        orders.add(new OrderSpecifier<>(DESC, payment.createdAt));
        orders.add(new OrderSpecifier<>(DESC, payment.updatedAt));

        for (Sort.Order sortOrder : pageable.getSort()) {
            com.querydsl.core.types.Order direction =
                    sortOrder.isAscending() ? ASC : DESC;
            switch (sortOrder.getProperty()) {
                case "createdAt" ->
                        orders.set(0, new OrderSpecifier<>(direction, payment.createdAt));
                case "updatedAt" ->
                        orders.set(1, new OrderSpecifier<>(direction, payment.updatedAt));
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

        // CUSTOMER : ONLINE 결제 , OWNER : OFFLINE 결제
        return switch (UserRole.fromString(role)) {
            case ROLE_CUSTOMER, ROLE_OWNER ->
                    payment.createdBy.eq(email);
            case ROLE_MANAGER, ROLE_MASTER ->
                    null;
        };
    }

    private BooleanExpression dateBetween(LocalDate startDate, LocalDate endDate) {

        BooleanExpression condition = payment.isNotNull();

        if (startDate != null) {
            condition = condition.and(
                    payment.createdAt.goe(LocalDateTime.of(startDate, LocalTime.MIDNIGHT))
                            .or(payment.updatedAt.goe(LocalDateTime.of(startDate, LocalTime.MIDNIGHT)))
            );
        }

        if (endDate != null) {
            condition = condition.and(
                    payment.createdAt.loe(LocalDateTime.of(endDate, LocalTime.MAX))
                            .or(payment.updatedAt.loe(LocalDateTime.of(endDate, LocalTime.MAX)))
            );
        }

        return condition;
    }
}
