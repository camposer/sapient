package com.sapient.com.sapient.model;

import org.hibernate.validator.constraints.LuhnCheck;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Other fields, like: expiration date or CVV where intentionally omitted
 */
@Entity
@Table(indexes = { @Index(columnList = "card_number") })
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "card_number", unique = true)
    @NotNull(message = "Card number is mandatory")
    @Pattern(regexp = "[0-9]{1,19}", message = "Card number has only numbers, cannot be empty and its max length is 19 chars")
    @LuhnCheck(message = "Card number has to be Luhn compliant")
    private String number;

    @NotNull(message = "Card holder name is mandatory")
    private String holderName;

    @Column(name = "card_limit")
    @NotNull(message = "Card limit is mandatory")
    private Double limit;

    private Double balance;

    public Card() {
        this.balance = 0d;
    }

    public Card(String number, String holderName, Double limit) {
        this();
        this.number = number;
        this.holderName = holderName;
        this.limit = limit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}
