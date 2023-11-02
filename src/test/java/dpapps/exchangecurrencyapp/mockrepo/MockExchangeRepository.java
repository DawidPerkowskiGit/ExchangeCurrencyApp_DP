package dpapps.exchangecurrencyapp.mockrepo;

import dpapps.exchangecurrencyapp.exchange.model.Exchange;
import dpapps.exchangecurrencyapp.exchange.repositories.ExchangeRepository;

import java.time.LocalDate;
import java.util.*;

public class MockExchangeRepository implements ExchangeRepository {

    List<Exchange> listOfExchanges = new ArrayList<>();


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
        for (Exchange exchange : this.listOfExchanges
        ) {
            if (Objects.equals(exchange.getId(), integer)) {
                return Optional.of(exchange);
            }
        }
        return Optional.of(null);
    }

    @Override
    public boolean existsById(Integer integer) {
        for (Exchange exchange : this.listOfExchanges
        ) {
            if (Objects.equals(exchange.getId(), integer)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterable<Exchange> findAll() {
        return this.listOfExchanges;
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

    @Override
    public boolean existsByExchangeDate(LocalDate date) {

        for (Exchange entry : listOfExchanges
        ) {
            if (entry.getExchangeDate().isEqual(date)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Exchange> findAllByExchangeDateOrderByCurrencyDesc(LocalDate date) {
        List<Exchange> dateList = new LinkedList<>();
        for (Exchange exchange : this.listOfExchanges
        ) {
            if (exchange.getExchangeDate().isEqual(date)) {
                dateList.add(exchange);
            }
        }
        dateList.sort(Comparator.comparing(a -> a.getCurrency().getIsoName()));
        return dateList;
    }

    @Override
    public List<Exchange> findAllByExchangeDateAndCurrencyOrderByExchangeDate(LocalDate date, String currency) {
        List<Exchange> dateList = new LinkedList<>();
        for (Exchange exchange : this.listOfExchanges
        ) {
            if (exchange.getExchangeDate().isEqual(date) && exchange.getCurrency().getIsoName().equals(currency)) {
                dateList.add(exchange);
            }
        }
        dateList.sort(Comparator.comparing(Exchange::getExchangeDate));
        return dateList;
    }

    @Override
    public List<Exchange> findAllByExchangeDateAndCurrenciesOrderByExchangeDate(LocalDate date, List<String> currency) {
        List<Exchange> dateList = new LinkedList<>();
        for (Exchange exchange : this.listOfExchanges
        ) {
            for (String singleCurrency : currency) {
                if (exchange.getExchangeDate().isEqual(date) && exchange.getCurrency().getIsoName().equals(singleCurrency)) {
                    dateList.add(exchange);
                }
            }
        }
        dateList.sort(Comparator.comparing(Exchange::getExchangeDate));
        return dateList;
    }

    @Override
    public Exchange findByExchangeDateAndCurrency_IsoName(LocalDate date, String currency) {
        for (Exchange exchange : this.listOfExchanges
        ) {
            if (exchange.getExchangeDate().isEqual(date) && exchange.getCurrency().getIsoName().equals(currency)) {
                return exchange;
            }
        }
        return null;
    }

    @Override
    public boolean existsByExchangeDateAndCurrency_IsoName(LocalDate date, String iso_name) {
        for (Exchange exchange : this.listOfExchanges
        ) {
            if (exchange.getExchangeDate().isEqual(date) && exchange.getCurrency().getIsoName().equals(iso_name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public LocalDate getLatestExchangeDate() {
        this.listOfExchanges.sort((a, b) -> a.getExchangeDate().compareTo(b.getExchangeDate()));
        return this.listOfExchanges.get(0).getExchangeDate();
    }
}
