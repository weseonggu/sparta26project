package com.sparta26.baemin.ai.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta26.baemin.dto.ai.QResponseSearchAiDto;
import com.sparta26.baemin.dto.ai.RequestSearchAiDto;
import com.sparta26.baemin.dto.ai.ResponseSearchAiDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.sparta26.baemin.ai.entity.QAi.ai;
import static org.springframework.util.StringUtils.hasText;

public class AiRepositoryImpl implements AiRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public AiRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseSearchAiDto> findAllAi(Pageable pageable, RequestSearchAiDto condition) {

        JPAQuery<ResponseSearchAiDto> query = queryFactory
                .select(new QResponseSearchAiDto(
                        ai.id,
                        ai.question,
                        ai.answer,
                        ai.createdAt,
                        ai.updatedAt
                ))
                .from(ai)
                .where(questionContains(condition.getQuestion()),
                        ai.isPublic.eq(true));

        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            sort.forEach(order -> {
                String property = order.getProperty();
                Sort.Direction direction = order.getDirection();

                if ("createdAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? ai.createdAt.asc() : ai.createdAt.desc());
                } else if ("updatedAt".equals(property)) {
                    query.orderBy(direction.isAscending() ? ai.updatedAt.asc() : ai.updatedAt.desc());
                }
            });
        }

        List<ResponseSearchAiDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(ai.count())
                .from(ai)
                .where(questionContains(condition.getQuestion()),
                        ai.isPublic.eq(true));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private BooleanExpression questionContains(String question) {
        return hasText(question) ? ai.question.contains(question) : null;
    }
}
