package com.test.account.infrastructure.adapters;

import com.test.account.application.mapper.MovementMapper;
import com.test.account.domain.model.Movement;
import com.test.account.domain.repository.MovementRepository;
import com.test.account.infrastructure.repositories.data.MovementData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class MovementRepositoryAdapter implements MovementRepository {

    private final R2dbcEntityTemplate template;
    private final MovementMapper mapper;

    @Override
    public Mono<Movement> save(Movement movement) {
        return template.insert(mapper.toData(movement))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Movement> findById(Long id) {
        return template.selectOne(
                Query.query(Criteria.where("id").is(id)),
                MovementData.class
        ).map(mapper::toDomain);
    }

    @Override
    public Flux<Movement> findAll() {
        return template.select(MovementData.class)
                .all()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Movement> update(Movement movement) {

        MovementData data = mapper.toData(movement);

        return template.update(
                        Query.query(Criteria.where("id").is(data.getId())),
                        Update.update("description", data.getDescription())
                                .set("movement_date", data.getMovementDate()),
                        MovementData.class
                )
                .flatMap(rows -> rows == 0
                        ? Mono.empty()
                        : findById(data.getId())
                );
    }

    @Override
    public Flux<Movement> findByAccountIdAndDateRange(Long accountId, LocalDateTime start, LocalDateTime end) {

        return template.select(MovementData.class)
                .matching(
                        Query.query(
                                Criteria.where("account_id").is(accountId)
                                        .and("movement_date").greaterThanOrEquals(start)
                                        .and("movement_date").lessThanOrEquals(end)
                        )
                )
                .all()
                .map(mapper::toDomain);
    }

}
