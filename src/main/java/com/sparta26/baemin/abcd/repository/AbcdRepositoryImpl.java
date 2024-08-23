package com.sparta26.baemin.abcd.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta26.baemin.abcd.entity.Abcd;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta26.baemin.abcd.entity.QAbcd.abcd;


@Repository
public class AbcdRepositoryImpl implements AbcdRepositoryCustom{

    private final JPAQueryFactory query;

    public AbcdRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }

    @Override
    public List<Abcd> findAll() {
        List<Abcd> fetch = query
                .select(abcd)
                .from(abcd)
                .fetch();

        return fetch;
    }


}

