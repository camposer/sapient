package com.sapient.rest.controller;

import com.sapient.com.sapient.model.Card;
import com.sapient.com.sapient.repository.CardRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CardRestControllerTest {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TestRestTemplate template;

    @LocalServerPort
    private int port;

    private final String validCardNumber = "4900000000000086";
    private final String cardHolderName = "John Lennon";
    private final Double cardLimit = 200d;

    private URL base;
    private Card validCard;

    @Before
    public void setUp() throws Exception {
        base = new URL("http://localhost:" + port + "/v1/cards");
        validCard = new Card(validCardNumber, cardHolderName, cardLimit);
    }

    @Test
    public void getCards_empty_success() {
        ResponseEntity<List> response = template.getForEntity(base.toString(), List.class);
        assertThat(response.getStatusCodeValue(), equalTo(200));
        assertThat(response.getBody().size(), equalTo(0));
    }

    @Test
    public void getCards_notEmpty_success() {
        cardRepository.save(validCard);

        Card expectedCard = new Card(validCardNumber, cardHolderName, cardLimit);
        expectedCard.setId(1L);

        ResponseEntity<List<Card>> response = template.exchange(base.toString(), HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Card>>(){});
        assertThat(response.getStatusCodeValue(), equalTo(200));
        assertThat(response.getBody().size(), equalTo(1));
        assertThat(response.getBody().get(0), samePropertyValuesAs(expectedCard));
    }

    @Test
    public void addCard_valid_success() throws Exception {
        ResponseEntity<String> response = template.postForEntity(base.toString(), validCard, String.class);
        assertThat(response.getStatusCodeValue(), equalTo(200));
        assertThat(response.getBody(), isEmptyOrNullString());

        Card expected = new Card(validCardNumber, cardHolderName, cardLimit);
        expected.setId(1L);

        Card actual = cardRepository.findByNumber(validCardNumber);
        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test
    public void addCard_withoutNumber_error() {
        Card cardWithoutNumber = new Card();
        cardWithoutNumber.setHolderName(cardHolderName);
        cardWithoutNumber.setLimit(cardLimit);

        ResponseEntity<String> response = template.postForEntity(base.toString(), cardWithoutNumber, String.class);
        assertThat(response.getStatusCodeValue(), equalTo(400));
        assertThat(response.getBody(), allOf(containsString("Card number"), containsString("mandatory")));

        boolean hasCards = cardRepository.findAll().iterator().hasNext();
        assertThat(hasCards, equalTo(false));
    }

    @Test
    public void addCard_withNumberNoLuhnCompliant_error() {
        Card cardWithNumberNoLuhnCompliant = new Card("4900000000000099", cardHolderName, cardLimit);

        ResponseEntity<String> response = template.postForEntity(base.toString(), cardWithNumberNoLuhnCompliant, String.class);
        assertThat(response.getStatusCodeValue(), equalTo(400));
        assertThat(response.getBody(), allOf(containsString("Card number"), containsString("Luhn")));

        boolean hasCards = cardRepository.findAll().iterator().hasNext();
        assertThat(hasCards, equalTo(false));
    }

    @Test
    public void addCard_withNumberLargerThan19Chars_error() {
        Card cardWithNumberLargerThan19Chars = new Card("12345678901234567890", cardHolderName, cardLimit);

        ResponseEntity<String> response = template.postForEntity(base.toString(), cardWithNumberLargerThan19Chars, String.class);
        assertThat(response.getStatusCodeValue(), equalTo(400));
        assertThat(response.getBody(), allOf(containsString("Card number"), containsString("19")));

        boolean hasCards = cardRepository.findAll().iterator().hasNext();
        assertThat(hasCards, equalTo(false));
    }

    @Test
    public void addCard_withEmptyNumber_error() {
        Card cardWithEmptyNumber = new Card("", cardHolderName, cardLimit);

        ResponseEntity<String> response = template.postForEntity(base.toString(), cardWithEmptyNumber, String.class);
        assertThat(response.getStatusCodeValue(), equalTo(400));
        assertThat(response.getBody(), allOf(containsString("Card number"), containsString("empty")));

        boolean hasCards = cardRepository.findAll().iterator().hasNext();
        assertThat(hasCards, equalTo(false));
    }

    @Test
    public void addCard_withDuplicatedNumber_error() {
        ResponseEntity<String> response = template.postForEntity(base.toString(), validCard, String.class);
        assertThat(response.getStatusCodeValue(), equalTo(200));
        assertThat(response.getBody(), isEmptyOrNullString());

        Card duplicatedValidCard = new Card(validCardNumber, "George Harrison", 300d);
        response = template.postForEntity(base.toString(), duplicatedValidCard, String.class);
        assertThat(response.getStatusCodeValue(), equalTo(500));
        assertThat(response.getBody(), allOf(containsString("Card number"), containsString("unique")));

        Card expected = new Card(validCardNumber, cardHolderName, cardLimit);
        expected.setId(1L);

        Card actual = cardRepository.findByNumber(validCardNumber);
        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test
    public void addCard_withoutHolderName_error() {
        Card cardWithoutHolderName = new Card();
        cardWithoutHolderName.setNumber(validCardNumber);
        cardWithoutHolderName.setLimit(cardLimit);

        ResponseEntity<String> response = template.postForEntity(base.toString(), cardWithoutHolderName, String.class);
        assertThat(response.getStatusCodeValue(), equalTo(400));
        assertThat(response.getBody(), allOf(containsString("Card holder name"), containsString("mandatory")));

        boolean hasCards = cardRepository.findAll().iterator().hasNext();
        assertThat(hasCards, equalTo(false));
    }

    @Test
    public void addCard_withoutLimit_error() {
        Card cardWithoutLimit = new Card();
        cardWithoutLimit.setNumber(validCardNumber);
        cardWithoutLimit.setHolderName(cardHolderName);

        ResponseEntity<String> response = template.postForEntity(base.toString(), cardWithoutLimit, String.class);
        assertThat(response.getStatusCodeValue(), equalTo(400));
        assertThat(response.getBody(), allOf(containsString("Card limit"), containsString("mandatory")));

        boolean hasCards = cardRepository.findAll().iterator().hasNext();
        assertThat(hasCards, equalTo(false));
    }

}
