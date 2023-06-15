package com.example.exchangecurrencyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;

@SpringBootApplication
public class ExchangeCurrencyAppApplication {

    public static void main(String[] args) throws FileNotFoundException {

/*        Parser parser = new Parser();
        parser.parse();*/
        SpringApplication.run(ExchangeCurrencyAppApplication.class, args);
    }

}
