package com.sparta26.baemin.repository.Abcd;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta26.baemin.entity.abcd.Abcd;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta26.baemin.entity.abcd.QAbcd.*;


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
