package ru.semavin.microservice.repositrories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.semavin.microservice.models.Subscription;
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
}
