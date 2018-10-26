package com.novoda.merlin;

public interface ResponseCodeValidator {

    boolean isResponseCodeValid(int responseCode);

    class CaptivePortalEndpointResponseCodeValidator implements ResponseCodeValidator {
        @Override
        public boolean isResponseCodeValid(int responseCode) {
            return responseCode == 204;
        }
    }
}
