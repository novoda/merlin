package com.novoda.merlin;

import java.util.Locale;

class MerlinMissingRegisterablesException extends IllegalStateException {

    private static final String REGISTERABLES_ASSERT_FORMAT = "You must call Merlin.Builder.with%sCallbacks before registering a %s";

    static MerlinMissingRegisterablesException missing(Class registerables) {
        String message = String.format(
                Locale.ENGLISH,
                REGISTERABLES_ASSERT_FORMAT,
                registerables.getSimpleName(),
                registerables.getSimpleName()
        );
        return new MerlinMissingRegisterablesException(message);
    }

    private MerlinMissingRegisterablesException(String message) {
        super(message);
    }
}
