package com.novoda.merlin.service;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

class LocalBinderDependencyMissingExceptionMatcher extends BaseMatcher<LocalBinderDependencyMissingException> {

    private String expectedMessage;
    private Throwable expectedCause;

    public static LocalBinderDependencyMissingExceptionMatcher from(Class expectedDependency) {
        LocalBinderDependencyMissingException expectedException = LocalBinderDependencyMissingException.missing(expectedDependency);
        return new LocalBinderDependencyMissingExceptionMatcher(expectedException);
    }

    private LocalBinderDependencyMissingExceptionMatcher(LocalBinderDependencyMissingException exception) {
        this.expectedMessage = exception.getMessage();
        this.expectedCause = exception.getCause();
    }

    @Override
    public boolean matches(Object o) {
        LocalBinderDependencyMissingException exception = (LocalBinderDependencyMissingException) o;
        return messageMatches(exception) && causeMatches(exception);
    }

    private boolean messageMatches(LocalBinderDependencyMissingException exception) {
        return expectedMessage.equals(exception.getMessage());
    }

    private boolean causeMatches(LocalBinderDependencyMissingException exception) {
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
                LocalBinderDependencyMissingException.class.getSimpleName(),
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
                    LocalBinderDependencyMissingException.class.getSimpleName(),
                    throwable.getMessage(),
                    throwable.getCause()
            );

            description.appendText(message);
        } else {
            super.describeMismatch(item, description);
        }
    }

}



