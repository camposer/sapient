package com.sapient.com.sapient.service;

import com.sapient.com.sapient.model.Card;

import java.util.List;

public interface CardService {

    void addCard(Card card);

    List<Card> getCards();

}
