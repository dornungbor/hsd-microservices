package com.dornu.hsd.server.domain.dao;

import com.dornu.hsd.server.domain.entities.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, String> {

}
