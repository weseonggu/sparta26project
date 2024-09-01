package com.sparta26.baemin.address.repository;

import com.sparta26.baemin.address.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    Page<Address> findAllByMemberIdAndIsPublic(Pageable page, Long id, boolean b);
}
