package com.dornu.hsd.server.domain.dao;

import com.dornu.hsd.server.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByEmail(String email);
}
