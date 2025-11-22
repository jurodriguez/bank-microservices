package com.test.account.infrastructure.adapters;

import com.test.account.application.mapper.AccountMapper;
import com.test.account.domain.model.Account;
import com.test.account.domain.repository.AccountRepository;
import com.test.account.infrastructure.repositories.data.AccountData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepository {

    private final R2dbcEntityTemplate template;
    private final AccountMapper mapper;

    @Override
    public Mono<Account> save(Account account) {
        return template.insert(mapper.toData(account))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Account> update(Account account) {

        AccountData data = mapper.toData(account);

        return template.update(
                        Query.query(Criteria.where("id").is(data.getId())),
                        Update.update("account_number", data.getAccountNumber())
                                .set("customer_id", data.getCustomerId())
                                .set("account_type", data.getAccountType())
                                .set("balance", data.getBalance())
                                .set("status", data.getStatus()),
                        AccountData.class
                )
                .flatMap(rows -> {
                    if (rows == 0) {
                        return Mono.empty();
                    }
                    return findById(data.getId());
                });
    }

    @Override
    public Mono<Account> findById(Long id) {
        return template.select(AccountData.class)
                .matching(Query.query(Criteria.where("id").is(id)))
                .one()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Account> findByAccountNumber(String accountNumber) {
        return template.select(AccountData.class)
                .matching(Query.query(Criteria.where("account_number").is(accountNumber)))
                .one()
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Account> findAll() {
        return template.select(AccountData.class)
                .all()
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Account> findByCustomerId(Long customerId) {
        return template.select(AccountData.class)
                .matching(Query.query(Criteria.where("customer_id").is(customerId)))
                .all()
                .map(mapper::toDomain);
    }

}
