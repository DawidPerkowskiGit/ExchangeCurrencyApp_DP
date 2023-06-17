package com.example.exchangecurrencyapp.exchange.repositories;

import com.example.exchangecurrencyapp.exchange.entities.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, Integer> {
}
