package com.novoda.merlin.service;

public interface ResponseCodeValidator {

    boolean isResponseCodeValid(int responseCode);

    class DefaultEndpointResponseCodeValidator implements ResponseCodeValidator {
        @Override
        public boolean isResponseCodeValid(int responseCode) {
            return responseCode == 204;
        }
    }

    class ResponseCodeAutoValidator implements ResponseCodeValidator {
        @Override
        public boolean isResponseCodeValid(int responseCode) {
            return true;
        }
    }
}
