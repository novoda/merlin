package com.novoda.merlin.service.request;

public class RequestException extends RuntimeException {
    RequestException(Throwable e) {
        super(e);
    }
}
