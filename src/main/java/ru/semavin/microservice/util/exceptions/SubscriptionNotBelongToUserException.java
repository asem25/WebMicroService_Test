package ru.semavin.microservice.util.exceptions;

public class SubscriptionNotBelongToUserException extends RuntimeException{
    public SubscriptionNotBelongToUserException(String message) {
        super(message);
    }
}
