package com.novoda.matcher;

import android.content.Intent;

import org.mockito.ArgumentMatcher;

import static org.mockito.Matchers.argThat;

class IntentStringExtraMatcher implements ArgumentMatcher<Intent> {

    private final Intent expected;
    private final String extraKey;

    public static Intent match(Intent intent, String extraKey) {
        return argThat(new IntentStringExtraMatcher(intent, extraKey));
    }

    private IntentStringExtraMatcher(Intent expected, String extraKey) {
        this.expected = expected;
        this.extraKey = extraKey;
    }

    @Override
    public boolean matches(Intent argument) {
        return argument.getStringExtra(extraKey).equals(expected.getStringExtra(extraKey));
    }
}
