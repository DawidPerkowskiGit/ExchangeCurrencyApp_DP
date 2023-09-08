package dpapps.exchangecurrencyapp.mockrepo;

import dpapps.exchangecurrencyapp.exchange.model.ApiKey;
import dpapps.exchangecurrencyapp.exchange.repositories.ApiKeyRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MockApiKeyRepo implements ApiKeyRepository {
    @Override
    public ApiKey findByValue(String value) {
        return null;
    }

    @Override
    public boolean existsByValue(String value) {
        return true;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends ApiKey> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends ApiKey> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<ApiKey> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public ApiKey getOne(Integer integer) {
        return null;
    }

    @Override
    public ApiKey getById(Integer integer) {
        return null;
    }

    @Override
    public ApiKey getReferenceById(Integer integer) {
        return null;
    }

    @Override
    public <S extends ApiKey> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ApiKey> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends ApiKey> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends ApiKey> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ApiKey> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ApiKey> boolean exists(Example<S> example) {
        return true;
    }

    @Override
    public <S extends ApiKey, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends ApiKey> S save(S entity) {
        return null;
    }

    @Override
    public <S extends ApiKey> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ApiKey> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return true;
    }

    @Override
    public List<ApiKey> findAll() {
        return null;
    }

    @Override
    public List<ApiKey> findAllById(Iterable<Integer> integers) {
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
    public void delete(ApiKey entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends ApiKey> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<ApiKey> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<ApiKey> findAll(Pageable pageable) {
        return null;
    }
}
