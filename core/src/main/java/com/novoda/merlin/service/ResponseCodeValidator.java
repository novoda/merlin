package com.novoda.merlin.service;

interface ResponseCodeValidator {
    boolean isResponseCodeValid(int responseCode);

    ResponseCodeValidator DEFAULT = new ResponseCodeValidator() {
        @Override
        public boolean isResponseCodeValid(int responseCode) {
            return responseCode == 204;
        }
    };

    ResponseCodeValidator CUSTOM = new ResponseCodeValidator() {
        @Override
        public boolean isResponseCodeValid(int responseCode) {
            return true;
        }
    };
}
