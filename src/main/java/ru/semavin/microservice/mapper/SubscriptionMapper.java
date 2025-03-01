package ru.semavin.microservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.semavin.microservice.dtos.SubscriptionDTO;
import ru.semavin.microservice.models.Subscription;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    Subscription toSubscription(SubscriptionDTO dto);
    @Mapping(source = "user.id", target = "userId")
    SubscriptionDTO toSubscriptionDTO(Subscription subscription);
}
