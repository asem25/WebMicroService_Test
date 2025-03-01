package ru.semavin.microservice.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.semavin.microservice.services.SubscriptionService;
import ru.semavin.microservice.services.UserService;

@TestConfiguration
public class TestConfig {

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    public SubscriptionService subscriptionService() {
        return Mockito.mock(SubscriptionService.class);
    }
}