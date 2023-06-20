package dpapps.exchangecurrencyapp.mockrepo;

import dpapps.exchangecurrencyapp.exchange.entities.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

public class MockExchangeRepository implements ExchangeRepository {

    List<Exchange> listOfExchanges = new ArrayList<>();

//    @Override
//    public void save(Exchange exchange) {
//        listOfExchanges.add(exchange);
//    }


    @Override
    public boolean existsByExchangeDate(LocalDate date) {
        return false;
    }

    @Override
    public <S extends Exchange> S save(S entity) {
        listOfExchanges.add(entity);
        return entity;
    }

    @Override
    public <S extends Exchange> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Exchange> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public Iterable<Exchange> findAll() {
        return null;
    }

    @Override
    public Iterable<Exchange> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(Exchange entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Exchange> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
