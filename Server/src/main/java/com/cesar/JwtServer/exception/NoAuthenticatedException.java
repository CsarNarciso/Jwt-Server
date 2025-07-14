package com.cesar.JwtServer.exception;

public class NoAuthenticatedException extends RuntimeException {
    public NoAuthenticatedException(String message){
        super(message);
    }
}