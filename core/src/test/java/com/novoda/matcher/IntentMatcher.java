package com.novoda.matcher;

import android.content.Intent;

import org.mockito.ArgumentMatcher;

import static org.mockito.Matchers.argThat;

class IntentMatcher implements ArgumentMatcher<Intent> {

    private final Intent expected;

    public static Intent match(Intent intent) {
        return argThat(new IntentMatcher(intent));
    }

    private IntentMatcher(Intent expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Intent argument) {
        return argument.getComponent().getClassName().equals(expected.getComponent().getClassName());
    }
}
