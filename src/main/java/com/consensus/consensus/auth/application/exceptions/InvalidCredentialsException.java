package com.consensus.consensus.auth.application.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Incorrect email or password.");
    }
}
