package com.example.exchangecurrencyapp.exchange.repositories;

import com.example.exchangecurrencyapp.exchange.entities.Exchange;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ExchangeRepository extends CrudRepository<Exchange, Integer> {
    boolean existsByExchangeDate(LocalDate date);

}
