package com.checkout.test.exception;

public class UnknownItemException extends RuntimeException {

    public UnknownItemException(String msg) {
        super(msg);
    }
}
