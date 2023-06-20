package dpapps.exchangecurrencyapp.mockrepo;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockCurrencyRepo implements CurrencyRepository {

    List<Currency> listOfCurrencies = new ArrayList<>();

    @Override
    public Currency save(Currency currency) {
        listOfCurrencies.add(currency);
        return currency;
    }

    @Override
    public boolean existsByIsoName(String name) {
        for (Currency entry: listOfCurrencies
             ) {
            if (entry.getIsoName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <S extends Currency> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Currency> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public Iterable<Currency> findAll() {
        return null;
    }

    @Override
    public Iterable<Currency> findAllById(Iterable<Integer> integers) {
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
    public void delete(Currency entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Currency> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Currency findCurrencyByIsoName(String name) {
        for (Currency entry: listOfCurrencies
             ) {
            if (entry.getIsoName().equals(name)) {
                return entry;
            }
        }
        return null;
    }
}
