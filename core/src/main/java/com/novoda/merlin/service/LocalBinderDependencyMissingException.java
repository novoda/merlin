package com.novoda.merlin.service;

import java.util.Locale;

class LocalBinderDependencyMissingException extends IllegalStateException {

    private static final String DEPENDENCY_ASSERT_FORMAT = "%s must be bound to %s.";

    static LocalBinderDependencyMissingException missing(Class dependency) {
        String message = String.format(
                Locale.ENGLISH,
                DEPENDENCY_ASSERT_FORMAT,
                dependency,
                MerlinService.class
        );
        return new LocalBinderDependencyMissingException(message);
    }

    private LocalBinderDependencyMissingException(String message) {
        super(message);
    }
}
