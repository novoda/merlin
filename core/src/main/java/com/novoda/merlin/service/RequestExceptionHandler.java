package com.novoda.merlin.service;

import com.novoda.merlin.MerlinLog;
import com.novoda.merlin.service.request.RequestException;

interface RequestExceptionHandler {
    boolean handleRequestException(RequestException e) throws RequestException;

    class DefaultEndpointRequestExceptionHandler implements RequestExceptionHandler {
        @Override
        public boolean handleRequestException(RequestException e) throws RequestException {
            return false;
        }
    }

    class CustomEndpointRequestExceptionHandler implements RequestExceptionHandler {
        @Override
        public boolean handleRequestException(RequestException e) throws RequestException {
            if (!e.causedByIO()) {
                MerlinLog.e("Ping task failed due to " + e.getMessage());
            }
            return false;
        }
    }
}
