package com.novoda.merlin.service;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

class MerlinServiceDependencyMissingExceptionMatcher extends BaseMatcher<MerlinServiceDependencyMissingException> {

    private String expectedMessage;
    private Throwable expectedCause;

    public static MerlinServiceDependencyMissingExceptionMatcher from(Class expectedDependency) {
        MerlinServiceDependencyMissingException expectedException = MerlinServiceDependencyMissingException.missing(expectedDependency);
        return new MerlinServiceDependencyMissingExceptionMatcher(expectedException);
    }

    private MerlinServiceDependencyMissingExceptionMatcher(MerlinServiceDependencyMissingException exception) {
        this.expectedMessage = exception.getMessage();
        this.expectedCause = exception.getCause();
    }

    @Override
    public boolean matches(Object o) {
        MerlinServiceDependencyMissingException exception = (MerlinServiceDependencyMissingException) o;
        return messageMatches(exception) && causeMatches(exception);
    }

    private boolean messageMatches(MerlinServiceDependencyMissingException exception) {
        return expectedMessage.equals(exception.getMessage());
    }

    private boolean causeMatches(MerlinServiceDependencyMissingException exception) {
        if (expectedCause == null) {
            return exception.getCause() == null;
        } else {
            return expectedCause.equals(exception.getCause());
        }
    }

    @Override
    public void describeTo(Description description) {
        String message = String.format(
                "%s: %s, cause: %s",
                MerlinServiceDependencyMissingException.class.getSimpleName(),
                expectedMessage,
                expectedCause
        );

        description.appendText(message);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        if (item instanceof Throwable) {
            Throwable throwable = (Throwable) item;
            String message = String.format(
                    "was: %s: %s, cause: %s",
                    MerlinServiceDependencyMissingException.class.getSimpleName(),
                    throwable.getMessage(),
                    throwable.getCause()
            );

            description.appendText(message);
        } else {
            super.describeMismatch(item, description);
        }
    }

}



