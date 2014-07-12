package com.novoda.merlin.service.request;

class RequestException extends RuntimeException {
    RequestException(Throwable e) {
        super(e);
    }
}
