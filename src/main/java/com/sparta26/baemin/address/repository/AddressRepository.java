package com.sparta26.baemin.address.repository;

import com.sparta26.baemin.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}
