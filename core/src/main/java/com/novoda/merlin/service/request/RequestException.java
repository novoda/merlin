package com.novoda.merlin.service.request;

import java.io.IOException;

public class RequestException extends RuntimeException {

    public RequestException(Throwable e) {
        super(e);
    }

    public boolean causedByIO() {
        return getCause() instanceof IOException;
    }
}
