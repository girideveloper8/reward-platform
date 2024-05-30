package com.retailer.reward.platform.exception;

public class InvalidTransactionAmountException extends RuntimeException {

    public InvalidTransactionAmountException(String message)
    {
        super(message);
    }
}
