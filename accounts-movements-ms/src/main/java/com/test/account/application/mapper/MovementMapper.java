package com.test.account.application.mapper;

import com.test.account.application.dto.MovementRequest;
import com.test.account.application.dto.MovementResponse;
import com.test.account.application.dto.MovementUpdateRequest;
import com.test.account.domain.model.Movement;
import com.test.account.infrastructure.repositories.data.MovementData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    //Request a Domain
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "balanceAfter", ignore = true)
    @Mapping(target = "movementDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Movement toDomain(MovementRequest request);

    //Domain a Data
    @Mapping(source = "movementType", target = "movementType")
    MovementData toData(Movement movement);

    //Data a Domain
    Movement toDomain(MovementData data);

    //Domain a Response
    @Mapping(source = "movementType", target = "movementType")
    MovementResponse toResponse(Movement movement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "movementType", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "balanceAfter", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Movement toEntity(MovementUpdateRequest request);

}
