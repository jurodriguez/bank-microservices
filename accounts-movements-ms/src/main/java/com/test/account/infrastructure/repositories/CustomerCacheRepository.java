package com.test.account.infrastructure.repositories;

import com.test.account.domain.model.CustomerSnapshot;
import com.test.account.infrastructure.events.shared.CustomerEvent;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CustomerCacheRepository {

    private final Map<Long, CustomerSnapshot> cache = new ConcurrentHashMap<>();

    public Mono<CustomerSnapshot> findById(Long id) {
        CustomerSnapshot snapshot = cache.get(id);
        return snapshot != null ? Mono.just(snapshot) : Mono.empty();
    }

    public Mono<Void> saveOrUpdate(CustomerEvent event) {
        CustomerSnapshot snapshot = new CustomerSnapshot();
        snapshot.setId(event.getId());
        snapshot.setName(event.getName());
        snapshot.setIdentification(event.getIdentification());
        snapshot.setStatus(event.getStatus());

        cache.put(event.getId(), snapshot);
        return Mono.empty();
    }

    public Mono<Void> delete(Long id) {
        cache.remove(id);
        return Mono.empty();
    }
}

