package com.dornu.hsd.server.domain.dao;

import com.dornu.hsd.server.domain.entities.CreditCard;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CreditCardDaoService {

    @PersistenceContext
    private EntityManager entityManager;

    public String insert(CreditCard creditCard) {
        entityManager.persist(creditCard);
        return creditCard.getId();
    }
}
