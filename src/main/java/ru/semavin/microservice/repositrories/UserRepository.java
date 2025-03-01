package ru.semavin.microservice.repositrories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.semavin.microservice.models.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Integer> {

}
