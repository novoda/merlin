package com.novoda.merlin.service;

import com.novoda.merlin.service.request.RequestException;

interface RequestExceptionHandler {
    boolean handleRequestException(RequestException e) throws RequestException;

    RequestExceptionHandler DEFAULT = new RequestExceptionHandler() {
        @Override
        public boolean handleRequestException(RequestException e) throws RequestException {
            return false;
        }
    };

    RequestExceptionHandler CUSTOM = new RequestExceptionHandler() {
        @Override
        public boolean handleRequestException(RequestException e) throws RequestException {
            if (e.causedByIO()) {
                return false;
            }
            throw e;
        }
    };
}
