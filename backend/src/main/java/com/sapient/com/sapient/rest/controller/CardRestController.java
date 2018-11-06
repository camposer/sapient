package com.sapient.com.sapient.rest.controller;

import com.sapient.com.sapient.model.Card;
import com.sapient.com.sapient.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/cards")
public class CardRestController {
    private static final Logger logger = LoggerFactory.getLogger(CardRestController.class);

    @Autowired
    private CardService cardService;
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<RestControllerResponse.Response> postCard(@Valid @RequestBody Card card) {
        try {
            cardService.addCard(card);
        } catch (Exception e) {
            logger.debug("Error adding card", e);
            return RestControllerResponse.error(e);
        }

        return RestControllerResponse.success();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Card> getCards() { return cardService.getCards(); }
    
}
