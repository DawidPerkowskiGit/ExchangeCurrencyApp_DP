package dpapps.exchangecurrencyapp.mockrepo;

import dpapps.exchangecurrencyapp.exchange.model.Currency;
import dpapps.exchangecurrencyapp.exchange.repositories.CurrencyRepository;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MockCurrencyRepository implements CurrencyRepository {

    List<Currency> currencyList = new LinkedList<>();
    @NonNull
    @Override
    public Currency save(@NonNull Currency currency) {
        this.currencyList.add(currency);
        return currency;
    }

    @Override
    public <S extends Currency> Iterable<S> saveAll(Iterable<S> entities) {
        for (S entity: entities
             ) {
            save(entity);
        }
        return null;
    }

    @Override
    public Optional<Currency> findById(Integer integer) {
        for (Currency currency: this.currencyList
             ) {
            if (currency.getId().equals(integer)) {
                return Optional.of(currency);
            }
        }
        return Optional.of(null);
    }

    @Override
    public boolean existsById(@NonNull Integer id) {
        for (Currency currency: this.currencyList
        ) {
            if (currency.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterable<Currency> findAll() {
        return this.currencyList;
    }

    @Override
    public Iterable<Currency> findAllById(Iterable<Integer> integers) {
        List<Currency> returnList = new LinkedList<>();
        for (Currency currency: this.currencyList
        ) {
            for (Integer integer: integers
                 ) {
                if (currency.getId().equals(integer)) {
                    returnList.add(currency);
                }
            }
        }
        return returnList;
    }

    @Override
    public long count() {
        int counter = 0;
        for (Currency currency: this.currencyList
        ) {
            counter++;
        }
        return counter;
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
        for (Currency currency: this.currencyList
             ) {
            if (currency.getIsoName().equals(name)) {
                return currency;
            }
        }
        return null;
    }

    @Override
    public List<Currency> getAll() {
        return this.currencyList;
    }

    @Override
    public List<String[]> getCurrenciesAndLocations() {
        return null;
    }

    @Override
    public List<Currency> getActivelyUsedCurrenciesAtDate(LocalDate date) {
        return null;
    }
}
