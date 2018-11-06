package com.sapient.com.sapient.repository;

import com.sapient.com.sapient.model.Card;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, Long> {
    Card findByNumber(String number);
}
