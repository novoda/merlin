package com.novoda.merlin.service;

interface ResponseCodeValidator {
    boolean isResponseCodeValid(int responseCode);

    class DefaultEndpointResponseCodeValidator implements ResponseCodeValidator {
        @Override
        public boolean isResponseCodeValid(int responseCode) {
            return responseCode == 204;
        }
    }

    class CustomEndpointResponseCodeValidator implements ResponseCodeValidator {
        @Override
        public boolean isResponseCodeValid(int responseCode) {
            return true;
        }
    }
}
