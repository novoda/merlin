package com.novoda.matcher;

import android.content.ComponentName;

import org.mockito.ArgumentMatcher;

import static org.mockito.Matchers.argThat;

public class ComponentNameMatcher extends ArgumentMatcher<ComponentName> {

    private final ComponentName expected;

    public static ComponentName match(ComponentName componentName) {
        return argThat(new ComponentNameMatcher(componentName));
    }

    private ComponentNameMatcher(ComponentName expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object o) {
        ComponentName actual = (ComponentName) o;

        return actual.getClassName().equals(expected.getClassName());
    }
}
