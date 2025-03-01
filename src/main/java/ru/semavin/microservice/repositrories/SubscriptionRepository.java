package ru.semavin.microservice.repositrories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.semavin.microservice.models.Subscription;
import ru.semavin.microservice.models.User;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUser(User user);
    @Query("SELECT COUNT(s) > 0 FROM Subscription s WHERE s.id = :subId AND s.user.id = :userId")
    boolean existsByIdAndUserId(@Param("subId") Long subId, @Param("userId") Long userId);
}
