package com.sparta26.baemin.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta26.baemin.entity.Member;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta26.baemin.entity.QMember.member;

@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory query;

    public MemberRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }

    @Override
    public List<Member> findAll() {
        List<Member> fetch = query
                .select(member)
                .from(member)
                .fetch();

        return fetch;
    }


}
