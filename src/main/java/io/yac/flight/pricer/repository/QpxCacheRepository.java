package io.yac.flight.pricer.repository;

import io.yac.flight.pricer.model.QpxCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QpxCacheRepository extends CrudRepository<QpxCache, Long> {

    Optional<QpxCache> findByRequestHash(int requestHash);
}
