package dpapps.exchangecurrencyapp.mockrepo;

import dpapps.exchangecurrencyapp.exchange.entities.Currency;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockCurrencyRepo implements CurrencyRepository {

    List<Currency> listOfCurrencies = new ArrayList<>();

    @NonNull
    @Override
    public Currency save(@NonNull Currency currency) {
        listOfCurrencies.add(currency);
        return currency;
    }

    @Override
    public <S extends Currency> Iterable<S> saveAll(Iterable<S> entities) {
        for (S entity: entities
             ) {
            this.save(entity);
        }
        return entities;
    }

    @Override
    public Optional<Currency> findById(Integer integer) {

        Optional<Currency> retVal;

        for (Currency currency: listOfCurrencies
             ) {
            if (currency.getId()==integer) {
                retVal = Optional.ofNullable(currency);
                return retVal;
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean existsById(@NonNull Integer id) {
        for (Currency currency: listOfCurrencies
             ) {
            if (currency.getId()==id) {
                return true;
            }
        }
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

    @Override
    public List<Currency> getAll() {
        return null;
    }

    @Override
    public List<String[]> getCurrenciesAndLocations() {
        return null;
    }
}
