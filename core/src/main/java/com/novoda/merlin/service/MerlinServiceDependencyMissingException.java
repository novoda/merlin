package com.novoda.merlin.service;

import java.util.Locale;

class MerlinServiceDependencyMissingException extends IllegalStateException {

    private static final String DEPENDENCY_ASSERT_FORMAT = "%s must be bound to %s.";

    static MerlinServiceDependencyMissingException missing(Class dependency) {
        String message = String.format(
                Locale.ENGLISH,
                DEPENDENCY_ASSERT_FORMAT,
                dependency,
                MerlinService.class
        );
        return new MerlinServiceDependencyMissingException(message);
    }

    private MerlinServiceDependencyMissingException(String message) {
        super(message);
    }
}
