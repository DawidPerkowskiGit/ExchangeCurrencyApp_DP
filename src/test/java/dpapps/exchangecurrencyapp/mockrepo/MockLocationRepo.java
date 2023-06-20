package dpapps.exchangecurrencyapp.mockrepo;

import dpapps.exchangecurrencyapp.exchange.entities.Location;
import dpapps.exchangecurrencyapp.exchange.repositories.LocationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockLocationRepo implements LocationRepository {

    private List<Location> locationList = new ArrayList<>();
    @Override
    public <S extends Location> S save(S entity) {
        locationList.add(entity);
        return entity;
    }

    @Override
    public <S extends Location> Iterable<S> saveAll(Iterable<S> entities) {
        for (S entity: entities
             ) {
            this.save(entity);
        }
        return entities;
    }

    @Override
    public Optional<Location> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public Iterable<Location> findAll() {
        return null;
    }

    @Override
    public Iterable<Location> findAllById(Iterable<Integer> integers) {
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
    public void delete(Location entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Location> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
