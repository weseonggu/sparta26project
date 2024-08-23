package com.sparta26.baemin.abcd.repository;

import com.sparta26.baemin.abcd.entity.Abcd;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbcdRepository extends JpaRepository<Abcd, Long>, AbcdRepositoryCustom {
}

