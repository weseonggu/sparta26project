package com.sparta26.baemin.repository.Abcd;

import com.sparta26.baemin.entity.abcd.Abcd;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbcdRepository extends JpaRepository<Abcd, Long>, AbcdRepositoryCustom {
}
