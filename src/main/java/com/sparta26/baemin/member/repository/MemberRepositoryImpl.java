package com.sparta26.baemin.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory query;

    public MemberRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }
}
