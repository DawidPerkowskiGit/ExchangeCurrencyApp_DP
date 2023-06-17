package com.example.exchangecurrencyapp;

import com.example.exchangecurrencyapp.exchange.entities.Currency;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class CurrencyJsonTests {
    @Autowired
    private JacksonTester<Currency> json;

    @Autowired
    private JacksonTester<Currency[]> jsonList;

    private Currency[] currencies;

    @BeforeEach
    void setUp() {
        currencies = Arrays.array(
                new Currency()
        );
    }

}
