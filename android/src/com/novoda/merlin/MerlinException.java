package com.novoda.merlin;

public class MerlinException extends RuntimeException {

    private final String message;

    public MerlinException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
