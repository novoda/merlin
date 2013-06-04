package com.novoda.matcher;

import android.content.Intent;

import org.mockito.ArgumentMatcher;

import static org.mockito.Matchers.argThat;

public class IntentMatcher extends ArgumentMatcher<Intent> {

    private final Intent expected;

    public static Intent match(Intent intent) {
        return argThat(new IntentMatcher(intent));
    }

    private IntentMatcher(Intent expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object o) {
        Intent actual = (Intent) o;

        return actual.getComponent().getClassName().equals(expected.getComponent().getClassName());
    }
}
