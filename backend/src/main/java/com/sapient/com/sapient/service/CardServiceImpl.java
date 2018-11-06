package com.sapient.com.sapient.service;

import com.sapient.com.sapient.model.Card;
import com.sapient.com.sapient.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Override
    public void addCard(Card card) {
        Card storedCard = cardRepository.findByNumber(card.getNumber());
        if (storedCard != null) {
            throw new RuntimeException("Card number must be unique");
        }
        cardRepository.save(card);
    }

    @Override
    public List<Card> getCards() {
        List<Card> cards = new ArrayList<>();
        Iterable<Card> cardsIterable = cardRepository.findAll();
        cardsIterable.forEach(cards::add);
        return cards;
    }

}
