package com.sparta26.baemin.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta26.baemin.dto.member.QResponseMemberInfoDto;
import com.sparta26.baemin.dto.member.RequestSearchMemberDto;
import com.sparta26.baemin.dto.member.ResponseMemberInfoDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sparta26.baemin.member.entity.QMember.member;
import static org.springframework.util.StringUtils.hasText;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseMemberInfoDto> findAllMember(Pageable pageable, RequestSearchMemberDto condition) {
       JPAQuery<ResponseMemberInfoDto> query = queryFactory
               .select(new QResponseMemberInfoDto(
                       member.id,
                       member.username,
                       member.nickname,
                       member.email,
                       member.password,
                       member.role,
                       member.createdAt,
                       member.createdBy,
                       member.updatedAt,
                       member.updatedBy,
                       member.deletedAt,
                       member.deletedBy,
                       member.isPublic
               ))
               .from(member)
               .where(emailEq(condition.getEmail()));
       Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            sort.forEach(order -> {
                String property = order.getProperty();
                Sort.Direction direction = order.getDirection();

                if ("createdAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? member.createdAt.asc() : member.createdAt.desc());
                } else if ("updatedAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? member.updatedAt.asc() : member.updatedAt.desc());
                } else {
                    query.orderBy(direction.isAscending() ? member.createdAt.asc() : member.createdAt.desc());
                }
            });
        }
        List<ResponseMemberInfoDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .where(emailEq(condition.getEmail()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);

    }

    private BooleanExpression emailEq(String email) {
        return hasText(email) ? member.email.eq(email) : null;
    }
}
