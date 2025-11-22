package com.test.account.application.mapper;

import com.test.account.application.dto.AccountRequest;
import com.test.account.application.dto.AccountResponse;
import com.test.account.domain.model.Account;
import com.test.account.infrastructure.repositories.data.AccountData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "balance", source = "initialBalance")
    Account toEntity(AccountRequest request);

    AccountResponse toResponse(Account account);

    AccountData toData(Account account);

    Account toDomain(AccountData data);
}


